param (
    [Parameter(Mandatory = $true, Position = 0)]
    [ValidateSet("Deploy", "Rollback", "Start", "Stop", "Restart")]
    [string]$action,
    [parameter(Mandatory = $false,
        ValueFromRemainingArguments = $true)]
    [String[]]
    $hints
)

$vb = $PSBoundParameters.ContainsKey('Verbose')

if ($vb) {
    $PSDefaultParameterValues['*:Verbose'] = $true
}

$hints | Out-String | Write-Verbose

$here = Split-Path -Parent $MyInvocation.MyCommand.Path
.  (Join-Path -Path $here -ChildPath "deploy-config.ps1")
.  (Join-Path -Path $here -ChildPath "SshInvoker.ps1")
. (Join-Path -Path $here -ChildPath "deploy-util.ps1")
$ConfigFile = Join-Path -Path $here -ChildPath "deploy.json"
$dconfig = Get-DeployConfig -ConfigFile $ConfigFile

switch ($action) {
    "Deploy" {
        Start-DeployServerSide -dconfig $dconfig -uploaded $hints[0]
        break
    }
    "Stop" {
        Stop-JavaProcess -dconfig $dconfig
        break
    }
    "Start" {
        Start-Tmux -dconfig $dconfig
        break
    }
    "Restart" {
        Stop-JavaProcess -dconfig $dconfig
        Start-Tmux -dconfig $dconfig
        break
    }
    Default {
        "unrecoginzed action: $action"
    }
}