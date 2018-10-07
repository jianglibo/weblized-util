$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$psDir = $here | Split-Path -Parent
$sut = "${here}\deploy-util.ps1"
. "$sut"
. "$here\SshInvoker.ps1"
. "$here\deploy-config.ps1"
$ConfigFile = "${psDir}\deploy.json"
$dconfig = Get-DeployConfig -ConfigFile $ConfigFile
Describe "copy scripts to server." {
    It "should copy" {
        Copy-PsScriptToServer -dconfig $dconfig
        $sshInvoker = Get-SshInvoker -dconfig $dconfig
        $sshInvoker.isFileExists($dconfig.scriptDir + "/deploy-config.ps1") | Should -BeTrue
        $sshInvoker.isFileExists($dconfig.scriptDir + "/deploy.json") | Should -BeTrue
    }
}