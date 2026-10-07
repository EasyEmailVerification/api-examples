<?php

function looks_like_email(string $address): bool
{
    return filter_var($address, FILTER_VALIDATE_EMAIL) !== false;
}

const EEV_API_URL = 'https://api.easyemailverification.com/v1/verify';

function eev_api_key(): string
{
    return getenv('EEV_API_KEY') ?: 'eev_sandbox_key';
}

function eev_request(string $url, ?array $body = null, int $timeout = 30): array
{
    $headers = ['X-API-Key: ' . eev_api_key()];
    $ch = curl_init($url);
    if ($body !== null) {
        $headers[] = 'Content-Type: application/json';
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($body));
    }
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_HTTPHEADER => $headers,
        CURLOPT_TIMEOUT => $timeout,
    ]);
    $raw = curl_exec($ch);
    if ($raw === false) {
        throw new RuntimeException('EEV request failed: ' . curl_error($ch));
    }
    $status = curl_getinfo($ch, CURLINFO_RESPONSE_CODE);
    $data = json_decode($raw, true);
    if ($status !== 200) {
        throw new RuntimeException('EEV error ' . $status . ': ' . ($data['message'] ?? 'unknown error'));
    }
    return $data;
}

function verify_email(string $address): array
{
    return eev_request(EEV_API_URL . '?email=' . rawurlencode($address));
}

function verify_emails(array $addresses): array
{
    return eev_request(EEV_API_URL, ['emails' => array_values($addresses)], 120); // up to 50 per request
}

function decide(array $result): string
{
    if ($result['did_you_mean'] !== '') {
        return 'suggest'; // ask the user: did you mean ...?
    }
    if ($result['result'] === 'valid' && $result['safe_to_send']) {
        return 'accept';
    }
    if ($result['result'] === 'invalid') {
        return 'reject';
    }
    return 'review'; // unknown, catch-all or disposable: your policy decides
}
