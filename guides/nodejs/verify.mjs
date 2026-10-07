const EMAIL_FORMAT = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function looksLikeEmail(address) {
  return EMAIL_FORMAT.test(address);
}

const API_URL = "https://api.easyemailverification.com/v1/verify";
const API_KEY = process.env.EEV_API_KEY ?? "eev_sandbox_key";

export async function verifyEmail(address) {
  const url = `${API_URL}?email=${encodeURIComponent(address)}`;
  const response = await fetch(url, {
    headers: { "X-API-Key": API_KEY },
    signal: AbortSignal.timeout(30_000),
  });
  const data = await response.json();
  if (!response.ok) {
    throw new Error(`EEV error ${response.status}: ${data.message}`);
  }
  return data;
}

export function decide(result) {
  if (result.did_you_mean) return "suggest"; // ask the user: did you mean ...?
  if (result.result === "valid" && result.safe_to_send) return "accept";
  if (result.result === "invalid") return "reject";
  return "review"; // unknown, catch-all or disposable: your policy decides
}

export async function verifyEmails(addresses) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: { "X-API-Key": API_KEY, "Content-Type": "application/json" },
    body: JSON.stringify({ emails: addresses }), // up to 50 per request
    signal: AbortSignal.timeout(120_000),
  });
  const data = await response.json();
  if (!response.ok) {
    throw new Error(`EEV error ${response.status}: ${data.message}`);
  }
  return data;
}
