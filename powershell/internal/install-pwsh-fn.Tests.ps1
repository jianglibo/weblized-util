$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut"

$kv = Get-Content .\properties-for-test.json | ConvertFrom-Json

Describe "install-pwsh" {
    It "does something useful" {
        $kv.atest  | Should -BeExactly '"a b c .""'
        $o = [Sshinvoker]::new($kv.hostname, $kv.ifile)
        $o.InvokeBash('yum remove powershell -y')
        $o.IsPwshInstalled() | Should -BeFalse
        Install-Pwsh -ifile $kv.ifile -HostName $kv.hostname
        $o.IsPwshInstalled() | Should -BeTrue
    }
}
