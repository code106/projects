Add-Type -TypeDefinition @"
    using System;
    using System.Runtime.InteropServices;
    public class KeyState {
        [DllImport("user32.dll", CharSet=CharSet.Auto, ExactSpelling=true)]
        public static extern short GetKeyState(int keyCode);
    }
"@ -Language CSharp

function Get-CapsLockState {
    [bool] $state = [System.Windows.Forms.Control]::IsKeyLocked('CapsLock')
    return $state
}

function Show-FullScreenImage {
    param (
        [string] $imagePath
    )

    Add-Type -AssemblyName System.Windows.Forms
    Add-Type -AssemblyName System.Drawing

    $image = [System.Drawing.Image]::FromFile($imagePath)
    $form = New-Object Windows.Forms.Form
    $form.FormBorderStyle = [Windows.Forms.FormBorderStyle]::None
    $form.WindowState = [Windows.Forms.FormWindowState]::Maximized

    $pictureBox = New-Object Windows.Forms.PictureBox
    $pictureBox.Image = $image
    $pictureBox.Dock = [Windows.Forms.DockStyle]::Fill

    $form.Controls.Add($pictureBox)

    $form.Add_Shown({$form.Activate()})
    $form.ShowDialog()
}

# Specify the path to the image file
$imagePath = "C:\path\to\your\image.jpg"

# Show the full-screen image until Caps Lock key is pressed
while (-not (Get-CapsLockState)) {
    Show-FullScreenImage -imagePath $imagePath
}

Write-Host "Caps Lock key pressed. Exiting..."
