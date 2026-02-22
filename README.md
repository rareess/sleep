# Sleep API

## Scripts

### generate-sleep-logs.ps1

Located in `scripts/generate-sleep-logs.ps1`.

Creates a user (`RaresTest`) and generates **N sleep log entries** for that user, one per day,
covering the range from `(yesterday - N + 1)` to `yesterday`. Prints the user ID at the end — use it with the other scripts.

**Parameter**

`-N` integer default: `7` — Number of sleep logs to generate

**Usage**

```powershell
# Generate 7 logs (default)
.\scripts\generate-sleep-logs.ps1

# Generate 40 logs
.\scripts\generate-sleep-logs.ps1 -N 40
```

---

### get-last-nights.ps1

Located in `scripts/get-last-nights.ps1`.

Fetches the last N sleep logs for a given user.

**Parameters**

`-u` string **required** - User ID
`-n` integer default: `7` - Number of nights to fetch

**Usage**

```powershell
.\scripts\generate-sleep-logs.ps1 -N 40
# get the printed user id and use below:
.\scripts\get-last-nights.ps1 -u <userId> -n 7
```

---

### get-averages.ps1

Located in `scripts/get-averages.ps1`.

Fetches the sleep averages for a given user over the last N days.

**Parameters**

`-u` string **required** - User ID
`-n` integer default: `30` - Number of days to average over

**Usage**

```powershell
.\scripts\get-averages.ps1 -u <userId> -n 30
```

---

**Typical flow**

```powershell
.\scripts\generate-sleep-logs.ps1 -N 40
# get the printed user id and use below:
.\scripts\get-last-nights.ps1 -u <userId> -n 7
.\scripts\get-averages.ps1    -u <userId> -n 30
```
