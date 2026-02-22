param(
    [Parameter(Mandatory)][string]$u,
    [int]$n = 7
)

$BASE_URL = "http://localhost:8080"

Write-Host "Fetching last $n nights for user $u..."
Invoke-RestMethod -Uri "$BASE_URL/api/users/$u/sleep-logs?nights=$n" | ConvertTo-Json
