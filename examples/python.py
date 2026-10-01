"""Easy Email Verification example using only the Python standard library."""

import json
import os
import sys
import urllib.parse
import urllib.request
import urllib.error

api_key = os.environ.get("EEV_API_KEY")
email = sys.argv[1] if len(sys.argv) > 1 else "user@example.com"

if not api_key:
    raise SystemExit(
        "EEV_API_KEY is not set. Create/manage a key at "
        "https://dashboard.easyemailverification.com/apisettings"
    )

query = urllib.parse.urlencode({"email": email, "apikey": api_key})
url = f"https://api.easyemailverification.com/v1/verify?{query}"

try:
    with urllib.request.urlopen(url, timeout=10) as response:
        result = json.load(response)
except urllib.error.HTTPError as exc:
    raise SystemExit(f"EEV request failed with HTTP {exc.code}") from exc
except (urllib.error.URLError, TimeoutError) as exc:
    raise SystemExit(f"Unable to verify email: {exc}") from exc

print(json.dumps(result, indent=2))

if result.get("result") == "unknown":
    print("Verification is inconclusive; do not automatically treat it as invalid.")
