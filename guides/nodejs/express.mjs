import express from "express";
import { looksLikeEmail, verifyEmail, decide } from "./verify.mjs";

const app = express();
app.use(express.json());

app.post("/signup", async (req, res) => {
  const email = String(req.body.email ?? "").trim();
  if (!looksLikeEmail(email)) {
    return res.status(400).json({ error: "Please enter a valid email address." });
  }
  let result;
  try {
    result = await verifyEmail(email);
  } catch {
    result = null; // verification unavailable: do not block the signup
  }
  const decision = result ? decide(result) : "review";
  if (decision === "reject") {
    return res.status(400).json({ error: "This email address cannot receive mail." });
  }
  if (decision === "suggest") {
    return res.status(400).json({ error: `Did you mean ${result.did_you_mean}?` });
  }
  // "accept" or "review": create the account; for "review", confirm the address by email
  res.json({ ok: true, needsConfirmation: decision === "review" });
});

app.listen(3000);
