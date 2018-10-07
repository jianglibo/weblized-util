param (
    [Parameter(Mandatory = $true, Position = 0)]
    [ValidateSet("InstallScripts", "Deploy", "Rollback", "Start", "Stop")]
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

$psDir = Split-Path -Parent $MyInvocation.MyCommand.Path
. "${psDir}\internal\deploy-config.ps1"
. "${psDir}\internal\SshInvoker.ps1"
. "${psDir}\internal\deploy-util.ps1"
$ConfigFile = "${psDir}\deploy.json"
$dconfig = Get-DeployConfig -ConfigFile $ConfigFile

switch ($action) {
    "InstallScripts" {
        Copy-PsScriptToServer -dconfig $dconfig
        break
    }
    "Deploy" {
        Start-DeployClientSide -dconfig $dconfig
        break
    }
    Default {
        Invoke-ServerRunningPs1 -dconfig $dconfig -action $action $hints
    }
}