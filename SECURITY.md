# Security

## API keys

Easy Email Verification API keys are secrets.

- Store keys in environment variables or a secrets manager.
- Do not commit `.env` files or credentials.
- Do not hard-code keys in source code.
- Do not expose keys in browser-side JavaScript.
- Do not embed keys in mobile application source.
- Avoid recording keys in application, proxy, analytics, or observability logs.

This repository uses `EEV_API_KEY` in examples.

If a key is accidentally committed, remove it from the repository history where appropriate and rotate/revoke the exposed credential.

Create or manage EEV API keys at:

https://dashboard.easyemailverification.com/apisettings
