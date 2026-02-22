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

$log1 = @{
    sleepDate   = (Get-Date).AddDays(-3).ToString("yyyy-MM-dd")
    bedTime     = "01:00:00"
    wakeTime    = "05:30:00"
    sleepStatus = "BAD"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$BASE_URL/api/users/$userId/sleep-logs" -Method POST `
    -ContentType "application/json" -Body $log1 | ConvertTo-Json

$log2 = @{
    sleepDate   = (Get-Date).AddDays(-2).ToString("yyyy-MM-dd")
    bedTime     = "23:30:00"
    wakeTime    = "07:00:00"
    sleepStatus = "OK"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$BASE_URL/api/users/$userId/sleep-logs" -Method POST `
    -ContentType "application/json" -Body $log2 | ConvertTo-Json

$log3 = @{
    sleepDate   = (Get-Date).AddDays(-1).ToString("yyyy-MM-dd")
    bedTime     = "22:00:00"
    wakeTime    = "07:30:00"
    sleepStatus = "GOOD"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$BASE_URL/api/users/$userId/sleep-logs" -Method POST `
    -ContentType "application/json" -Body $log3 | ConvertTo-Json

Write-Host ""
Write-Host " Fetching last 2 nights for user $userId..."
Invoke-RestMethod -Uri "$BASE_URL/api/users/$userId/sleep-logs?nights=2" | ConvertTo-Json

