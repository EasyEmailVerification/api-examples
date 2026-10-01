package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"os"
	"time"
)

func main() {
	apiKey := os.Getenv("EEV_API_KEY")
	email := "user@example.com"
	if len(os.Args) > 1 {
		email = os.Args[1]
	}

	if apiKey == "" {
		fmt.Fprintln(os.Stderr,
			"EEV_API_KEY is not set. Create/manage a key at "+
				"https://dashboard.easyemailverification.com/apisettings")
		os.Exit(1)
	}

	u, _ := url.Parse("https://api.easyemailverification.com/v1/verify")
	q := u.Query()
	q.Set("email", email)
	q.Set("apikey", apiKey)
	u.RawQuery = q.Encode()

	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Get(u.String())
	if err != nil {
		fmt.Fprintln(os.Stderr, "Unable to verify email:", err)
		os.Exit(1)
	}
	defer resp.Body.Close()

	if resp.StatusCode < 200 || resp.StatusCode >= 300 {
		fmt.Fprintf(os.Stderr, "EEV request failed with HTTP %d\n", resp.StatusCode)
		os.Exit(1)
	}

	var result map[string]any
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Fprintln(os.Stderr, "Unable to decode response:", err)
		os.Exit(1)
	}

	out, _ := json.MarshalIndent(result, "", "  ")
	fmt.Println(string(out))

	if result["result"] == "unknown" {
		fmt.Println("Verification is inconclusive; do not automatically treat it as invalid.")
	}
}
