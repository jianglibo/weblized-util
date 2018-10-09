$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$psDir = $here | Split-Path -Parent
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut"

Describe "deploy-config" {
    It "should load default config" {
        $r = Get-DeployConfig -ConfigFile "${psDir}\deploy-dev.json"
        $r.config.basedir | Should -Be '/opt/weblized-util'
    }
}
