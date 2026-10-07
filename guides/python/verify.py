import os
import re

import requests

EMAIL_FORMAT = re.compile(r"^[^\s@]+@[^\s@]+\.[^\s@]+$")


def looks_like_email(address: str) -> bool:
    """Cheap first filter: something@something.something, no spaces."""
    return bool(EMAIL_FORMAT.match(address))


API_URL = "https://api.easyemailverification.com/v1/verify"
API_KEY = os.environ.get("EEV_API_KEY", "eev_sandbox_key")


def verify_email(address: str) -> dict:
    response = requests.get(
        API_URL,
        params={"email": address},  # requests URL-encodes it (+ becomes %2B)
        headers={"X-API-Key": API_KEY},
        timeout=30,
    )
    data = response.json()
    if response.status_code != 200:
        raise RuntimeError(f"EEV error {response.status_code}: {data.get('message')}")
    return data


def decide(result: dict) -> str:
    if result["did_you_mean"]:
        return "suggest"  # ask the user: did you mean ...?
    if result["result"] == "valid" and result["safe_to_send"]:
        return "accept"
    if result["result"] == "invalid":
        return "reject"
    return "review"  # unknown, catch-all or disposable: your policy decides


def verify_emails(addresses: list[str]) -> list[dict]:
    response = requests.post(
        API_URL,
        json={"emails": addresses},  # up to 50 per request
        headers={"X-API-Key": API_KEY},
        timeout=120,
    )
    data = response.json()
    if response.status_code != 200:
        raise RuntimeError(f"EEV error {response.status_code}: {data.get('message')}")
    return data

