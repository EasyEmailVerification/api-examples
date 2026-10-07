# Email validation guides: code

Tested code from the email validation guides on easyemailverification.com. Each one checks the
format first, then the mailbox with the Easy Email Verification API, and turns the result into
`accept`, `reject`, `suggest` (typo correction) or `review` (unknown, catch-all or disposable).

| Language | Code | Guide |
|---|---|---|
| Node.js (fetch, Express) | [`nodejs/`](nodejs/) | https://www.easyemailverification.com/en-US/guides/validate-email-nodejs |
| Python (requests) | [`python/`](python/) | https://www.easyemailverification.com/en-US/guides/validate-email-python |
| Java (Spring Boot 3, RestClient) | [`java-spring-boot/`](java-spring-boot/) | https://www.easyemailverification.com/en-US/guides/validate-email-java-spring-boot |
| PHP (filter_var, cURL) | [`php/`](php/) | https://www.easyemailverification.com/en-US/guides/validate-email-php |

Without `EEV_API_KEY` set, the code uses the public sandbox key `eev_sandbox_key`, which answers
for test addresses such as `valid@sandbox.easyemailverification.com`,
`invalid@sandbox.easyemailverification.com`, `unknown@sandbox.easyemailverification.com` and
`typo@gmial.com`, without an account or credits.

What each result means: https://www.easyemailverification.com/en-US/help/result-codes
