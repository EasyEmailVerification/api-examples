// Node.js 18+ (uses the built-in fetch API)
const apiKey = process.env.EEV_API_KEY;
const email = process.argv[2] ?? "user@example.com";

if (!apiKey) {
  console.error(
    "EEV_API_KEY is not set. Create/manage a key at " +
    "https://dashboard.easyemailverification.com/apisettings"
  );
  process.exit(1);
}

const url = new URL("https://api.easyemailverification.com/v1/verify");
url.searchParams.set("email", email);
url.searchParams.set("apikey", apiKey);

const controller = new AbortController();
const timeout = setTimeout(() => controller.abort(), 10_000);

try {
  const response = await fetch(url, { signal: controller.signal });

  if (!response.ok) {
    throw new Error(`EEV request failed with HTTP ${response.status}`);
  }

  const result = await response.json();
  console.log(JSON.stringify(result, null, 2));

  // Example only: choose policy according to your application's risk requirements.
  if (result.result === "unknown") {
    console.log("Verification is inconclusive; do not automatically treat it as invalid.");
  }
} catch (error) {
  console.error("Unable to verify email:", error.message);
  process.exitCode = 1;
} finally {
  clearTimeout(timeout);
}
