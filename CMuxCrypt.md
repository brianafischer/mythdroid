## Secure connectivity via CMux ##

MDD generates a random, 128 bit key at install time and then hands it out to
MythDroid when it connects from a private address (inside your LAN).

When MythDroid later connects to CMux from outside the LAN CMux generates and sends
a random nonce, decrypts the response with AES and checks that it matches, thereby
authenticating the remote MythDroid. All communications are thereafter encrypted
with AES in CFB.

CMux won't try to secure connections from localhost so if you've been using
SSH and port forwarding you can elect to continue doing so. However, this
feature means that you could forego SSH and forward port 16550/tcp to your backend at your router, which is much more convenient.

### Note ###

By using CMux's crypt functionality, you accept that anyone who has run MythDroid inside your LAN will be able to access it from outside the LAN too (unless you change the key when they leave!).