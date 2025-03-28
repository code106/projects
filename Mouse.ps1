Add-Type -TypeDefinition @"
    using System;
    using System.Runtime.InteropServices;
    public class Mouse {
        [DllImport("user32.dll")]
        public static extern void SetCursorPos(int x, int y);
    }
"@

while ($true) {
    $pos = [System.Windows.Forms.Cursor]::Position
    [Mouse]::SetCursorPos($pos.X + 1, $pos.Y)  # Moves cursor slightly
    Start-Sleep -Seconds 60
    [Mouse]::SetCursorPos($pos.X, $pos.Y)      # Moves it back
    Start-Sleep -Seconds 60
}


Add-Type -AssemblyName System.Windows.Forms

while ($true) {
    $pos = [System.Windows.Forms.Cursor]::Position
    [System.Windows.Forms.Cursor]::Position = New-Object System.Drawing.Point($pos.X + 1, $pos.Y)
    Start-Sleep -Seconds 60
    [System.Windows.Forms.Cursor]::Position = New-Object System.Drawing.Point($pos.X, $pos.Y)
    Start-Sleep -Seconds 60
}

Add-Type -TypeDefinition @"
    using System;
    using System.Runtime.InteropServices;
    public class Keyboard {
        [DllImport("user32.dll")]
        public static extern void keybd_event(byte bVk, byte bScan, uint dwFlags, int dwExtraInfo);
    }
"@
while ($true) {
    [Keyboard]::keybd_event(0x91, 0, 1, 0)  # Simulates pressing Scroll Lock
    Start-Sleep -Seconds 60
    [Keyboard]::keybd_event(0x91, 0, 1, 0)  # Simulates pressing Scroll Lock again
    Start-Sleep -Seconds 60
}

