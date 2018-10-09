$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$psDir = $here | Split-Path -Parent
$sut = "${here}\deploy-util.ps1"
. "$sut"
. "$here\SshInvoker.ps1"
. "$here\deploy-config.ps1"
$ConfigFile = "${psDir}\deploy-dev.json"
$dconfig = Get-DeployConfig -ConfigFile $ConfigFile

Describe "copy remote scripts" {
    It "should upload config value and work." {
        $ConfigFile = "${psDir}\deploy-dev.json"
        Get-Content -Path $ConfigFile | Out-Host
        $dconfig = Get-DeployConfig -ConfigFile $ConfigFile
        $sshInvoker = Get-SshInvoker -dconfig $dconfig
        $rscript = New-RemoteScriptFile -sshInvoker $sshInvoker -DeployConfig $dconfig -scriptLines '$dconfigstr'
        $rr = $sshInvoker.invoke("pwsh -f $rscript")
        $rdconfig = $rr | ConvertFrom-Json
        $rdconfig.config.basedir | Should -Be '/opt/weblized-util'
    }
}

Describe "deploy to server." {
    It "should start deploy" {
        $sshInvoker = Get-SshInvoker -dconfig $dconfig
        $rd = "rm -rf {0}/*" -f $dconfig.config.basedir
        $sshInvoker.invoke($rd)

        Start-DeployClientSide -dconfig $dconfig | Out-Host
        $sshInvoker.isFileExists($dconfig.workingDir) | Should -BeTrue
    }
}
