param (
    [Parameter(Mandatory = $true, Position = 0)]
    [ValidateSet("Deploy", "Rollback", "Start", "Stop")]
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
# $dconfig = Get-Content -Path $ConfigFile | ConvertFrom-Json
# $dconfig
# bellow line is wrong. it's client side code.

switch ($action) {
    "Deploy" {
        Start-DeployServerSide -dconfig $dconfig -uploaded $hints[0]
    }
    "Stop" {
        Stop-JavaProcess -dconfig $dconfig
    }
    Default {
        "unrecoginzed action: $action"
    }
}