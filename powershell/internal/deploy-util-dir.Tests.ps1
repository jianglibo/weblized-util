$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$psDir = $here | Split-Path -Parent
$sut = "${here}\deploy-util.ps1"
. "$sut"
. "$here\SshInvoker.ps1"
. "$here\deploy-config.ps1"
$ConfigFile = "${psDir}\deploy.json"

Describe "deploy-util" {
    $workingDir = Join-Path $TestDrive "folder\working"
    New-Item -ItemType Directory -Path $workingDir
    $jar1 = Join-Path -Path $workingDir -ChildPath "10.jar"
    "abc" | Out-File $jar1
    Start-Sleep -Seconds 1
    $jar2 = Join-Path -Path $workingDir -ChildPath "2.jar"
    "abc" | Out-File $jar2

    it "shoud find newest jar file." {
        $f = Find-NewestByExt -Path $workingDir -Ext "jar"
        $f.Name | Should -Be "2.jar"
    }

    it "should join path" {
        Join-UniversalPath -Path "a" -ChildPath "b" | Should -Be "a\b"
        Join-UniversalPath -Path "a\\" -ChildPath "\\\b" | Should -Be "a\b"

        Join-UniversalPath -Path "a//" -ChildPath "//b" | Should -Be "a/b"
        Join-UniversalPath -Path "a\/" -ChildPath "//b" | Should -Be "a/b"
        Join-UniversalPath -Path "a\\" -ChildPath "//b" | Should -Be "a\b"
    }

    it "should split all path parent" {
        Split-UniversalPath -Path "a\b\c" -Parent | Should -Be "a\b"
        Split-UniversalPath -Path "a\\\\b\c" -Parent | Should -Be "a\b"
        Split-UniversalPath -Path "a" -Parent | Should -Be "a"
        Split-UniversalPath -Path "a\\" -Parent | Should -Be "a"

        Split-UniversalPath -Path "a/b/c" -Parent | Should -Be "a/b"
        Split-UniversalPath -Path "a////b/c" -Parent | Should -Be "a/b"
        Split-UniversalPath -Path "a/////" -Parent | Should -Be "a"

        Split-UniversalPath -Path "a\b/c" -Parent | Should -Be "a/b"
        Split-UniversalPath -Path "a/\//b\c" -Parent | Should -Be "a/b"
        Split-UniversalPath -Path "a//\//" -Parent | Should -Be "a"

    }
    it "should split all path filename" {
        Split-UniversalPath -Path "a\b\c" | Should -Be "c"
        Split-UniversalPath -Path "a\\\\b\c" | Should -Be "c"
        Split-UniversalPath -Path "a" | Should -Be "a"
        Split-UniversalPath -Path "a\\"  | Should -Be "a"

        Split-UniversalPath -Path "a/b/c" | Should -Be "c"
        Split-UniversalPath -Path "a////b/c" | Should -Be "c"
        Split-UniversalPath -Path "a/////" | Should -Be "a"

        Split-UniversalPath -Path "a\b/c" | Should -Be "c"
        Split-UniversalPath -Path "a/\//b\c" | Should -Be "c"
        Split-UniversalPath -Path "a//\//" | Should -Be "a"

    }
}
