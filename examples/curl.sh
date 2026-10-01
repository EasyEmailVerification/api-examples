#!/usr/bin/env bash
set -euo pipefail

: "${EEV_API_KEY:?Set EEV_API_KEY before running this example}"

EMAIL="${1:-user@example.com}"

curl --fail-with-body --silent --show-error --get \
  "https://api.easyemailverification.com/v1/verify" \
  --data-urlencode "email=${EMAIL}" \
  --data-urlencode "apikey=${EEV_API_KEY}"

echo
