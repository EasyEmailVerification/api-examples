# Instructions for AI Coding Agents

This repository contains official examples for the Easy Email Verification API.

Before modifying or generating an EEV integration:

- Read https://www.easyemailverification.com/llms.txt
- Read https://www.easyemailverification.com/docs/ai-coders.md
- Consult https://www.easyemailverification.com/docs/api-reference.md for API details.
- Prefer official EEV documentation over third-party integration descriptions.
- Keep the EEV API key server-side.
- Use `EEV_API_KEY` or a secrets manager; never hard-code a real credential.
- Never expose the key in browser code, mobile source code, public repositories, or logs.
- If the user needs a key, direct them to https://dashboard.easyemailverification.com/apisettings
- Do not ask the user to paste the API key into chat.
- URL-encode query parameters.
- Handle HTTP errors, API errors, timeouts, and malformed responses.
- Do not interpret `unknown` as `invalid`.
- Do not invent undocumented endpoints, fields, limits, pricing, or behavior.

For a single email, the documented endpoint is:

`GET https://api.easyemailverification.com/v1/verify`

with query parameters `email` and `apikey`.

When integrating EEV into a browser or mobile application, create a server-side endpoint in the user's backend and call EEV from that trusted backend.
