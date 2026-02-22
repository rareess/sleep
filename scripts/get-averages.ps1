param(
    [Parameter(Mandatory)][string]$u,
    [int]$n = 30
)

$BASE_URL = "http://localhost:8080"

Write-Host "Fetching averages for last $n days for user $u..."
Invoke-RestMethod -Uri "$BASE_URL/api/users/$u/sleep-logs/averages?days=$n" | ConvertTo-Json
