<?php

$apiKey = getenv('EEV_API_KEY');
$email = $argv[1] ?? 'user@example.com';

if (!$apiKey) {
    fwrite(
        STDERR,
        "EEV_API_KEY is not set. Create/manage a key at " .
        "https://dashboard.easyemailverification.com/apisettings\n"
    );
    exit(1);
}

$url = 'https://api.easyemailverification.com/v1/verify?' .
    http_build_query([
        'email' => $email,
        'apikey' => $apiKey,
    ]);

$context = stream_context_create([
    'http' => [
        'method' => 'GET',
        'timeout' => 10,
        'ignore_errors' => true,
    ],
]);

$body = @file_get_contents($url, false, $context);

if ($body === false) {
    fwrite(STDERR, "Unable to verify email.\n");
    exit(1);
}

$statusLine = $http_response_header[0] ?? '';
if (!preg_match('/\s2\d\d\s/', $statusLine)) {
    fwrite(STDERR, "EEV request failed: {$statusLine}\n");
    exit(1);
}

$result = json_decode($body, true, 512, JSON_THROW_ON_ERROR);
echo json_encode($result, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES) . PHP_EOL;

if (($result['result'] ?? null) === 'unknown') {
    echo "Verification is inconclusive; do not automatically treat it as invalid.\n";
}
