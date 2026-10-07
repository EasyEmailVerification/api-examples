# Easy Email Verification — Official API Examples

Official, copy-paste examples for integrating the [Easy Email Verification](https://www.easyemailverification.com/) REST API.

Easy Email Verification (EEV) helps applications verify email addresses, reduce invalid signups and bounces, detect risky addresses, and improve email deliverability.

## Quick start

### 1. Get an API key

Create or manage your API key:

https://dashboard.easyemailverification.com/apisettings

Never commit an API key to Git or expose it in frontend/browser code.

### 2. Set the environment variable

macOS / Linux:

```bash
export EEV_API_KEY="your_api_key"
```

Windows PowerShell:

```powershell
$env:EEV_API_KEY="your_api_key"
```

### 3. Try the API

```bash
curl --get "https://api.easyemailverification.com/v1/verify" \
  --data-urlencode "email=user@example.com" \
  --data-urlencode "apikey=$EEV_API_KEY"
```

## Examples

| Language | Example |
|---|---|
| cURL | [`examples/curl.sh`](examples/curl.sh) |
| JavaScript / Node.js | [`examples/node.mjs`](examples/node.mjs) |
| Python | [`examples/python.py`](examples/python.py) |
| PHP | [`examples/php.php`](examples/php.php) |
| Java | [`examples/VerifyEmail.java`](examples/VerifyEmail.java) |
| Go | [`examples/go.go`](examples/go.go) |

Complete signup validation examples (format check, mailbox check, decision) for Node.js, Python, Spring Boot and PHP are in [`guides/`](guides/), with the matching guides on the website.

All examples use the official endpoint:

```text
GET https://api.easyemailverification.com/v1/verify
```

Required query parameters:

- `email` — email address to verify
- `apikey` — your Easy Email Verification API key

## Example response

A response may look like:

```json
{
  "email": "user@example.com",
  "result": "valid",
  "reason": "accepted_email",
  "disposable": false,
  "accept_all": false,
  "role": false,
  "free": false,
  "safe_to_send": true,
  "success": true
}
```

The API may return additional fields. Do not treat this example as the exhaustive response schema.

## Important result fields

- `result` — overall verification result
- `reason` — reason for the result
- `safe_to_send` — whether EEV considers the address safe for sending
- `disposable` — disposable email provider indicator
- `accept_all` — accept-all/catch-all domain indicator
- `role` — role-based address indicator
- `did_you_mean` — suggested correction when available

**Do not automatically treat `unknown` as `invalid`.** An unknown result means EEV could not make a definitive determination.

Your application should decide how to use `result`, `reason`, and `safe_to_send` according to its own risk requirements.

## Browser and mobile applications

Do **not** call EEV directly from public browser JavaScript or embed an API key in a mobile application.

Use this architecture:

```text
Browser / Mobile App
        |
        v
Your trusted backend
        |
        v
Easy Email Verification API
```

## AI coding assistants

If you are Codex, Claude Code, Cursor, GitHub Copilot, or another coding agent:

1. Read https://www.easyemailverification.com/llms.txt
2. Read https://www.easyemailverification.com/docs/ai-coders.md
3. Use https://www.easyemailverification.com/docs/api-reference.md for the concise API reference.
4. Use the official Stoplight documentation when more detail is required.
5. Never invent undocumented endpoints, parameters, response values, limits, or pricing.
6. Never ask a user to paste an API key into chat. Direct the user to https://dashboard.easyemailverification.com/apisettings and use an environment variable such as `EEV_API_KEY`.

See [`AGENTS.md`](AGENTS.md).

## Bulk verification

EEV also provides a Bulk API for validating email lists. For current bulk endpoints, parameters, file formats, job status, download and deletion behavior, use the official API documentation rather than implementing a client-side loop based on this repository.

## Official documentation

- LLM entry point: https://www.easyemailverification.com/llms.txt
- AI coding guide: https://www.easyemailverification.com/docs/ai-coders.md
- API reference: https://www.easyemailverification.com/docs/api-reference.md
- Full API docs: https://eev.stoplight.io/docs/eev/122963476e10f-getting-started
- Website: https://www.easyemailverification.com/

## Security

See [`SECURITY.md`](SECURITY.md).

## License

MIT. See [`LICENSE`](LICENSE).
