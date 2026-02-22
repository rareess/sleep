param(
    [int]$N = 7
)

$BASE_URL = "http://localhost:8080"

$userBody = @{
    username  = "RaresTest"
    firstName = "Rares"
    lastName  = "Test"
    email     = "rares.test@test.com"
} | ConvertTo-Json

$userResponse = Invoke-RestMethod -Uri "$BASE_URL/api/users" -Method POST `
    -ContentType "application/json" -Body $userBody

$userResponse | ConvertTo-Json
$userId = $userResponse.id
Write-Host "User ID: $userId"

$statuses = @("BAD", "OK", "GOOD")

for ($i = $N; $i -ge 1; $i--) {
    $sleepDate = (Get-Date).AddDays(-$i).ToString("yyyy-MM-dd")
    $status    = $statuses[($i - 1) % 3]

    $bedHour  = Get-Random -Minimum 21 -Maximum 24
    $bedMin   = @(0, 15, 30, 45) | Get-Random
    $wakeHour = Get-Random -Minimum 5 -Maximum 9
    $wakeMin  = @(0, 15, 30, 45) | Get-Random

    $logBody = @{
        sleepDate   = $sleepDate
        bedTime     = "{0:D2}:{1:D2}:00" -f $bedHour, $bedMin
        wakeTime    = "{0:D2}:{1:D2}:00" -f $wakeHour, $wakeMin
        sleepStatus = $status
    } | ConvertTo-Json

    Write-Host "Posting log for $sleepDate ($status)..."
    Invoke-RestMethod -Uri "$BASE_URL/api/users/$userId/sleep-logs" -Method POST `
        -ContentType "application/json" -Body $logBody | ConvertTo-Json
}

Write-Host ""
Write-Host "Done. User ID: $userId"
