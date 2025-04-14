# leJOS NXJ Installation Guide on macOS (64-bit)

> **Note:** This guide assumes you have the Java Development Kit (JDK) installed and that you have extracted the leJOS NXJ package.

## 1. Download the Package

Download the latest leJOS NXJ package for macOS from the [leJOS website](https://lejos.sourceforge.io/nxj-downloads.php).

## 2. Extract the Package

Open your Terminal and run (replace `<version>` with the actual version number):

```bash
tar -xzvf leJOS_NXJ_<version>.tar.gz -C ~/lejos
```

Into a directory (e.g., `~/lejos`).

## 3. Set Up Environment Variables)

Edit your `~/.zshrc` or `~/.bashrc` file:

```bash
nano ~/.zshrc
```

Add the following lines:

```bash
export LEJOS_NXJ=~/lejos
export PATH=$PATH:$LEJOS_NXJ/bin
```

Reload your configuration:

```bash
source ~/.zshrc
```

## 4. Modify the `nxj` Script

The default `nxj` script forces 32‑bit mode on macOS, which conflicts with your 64‑bit JDK. To fix this:

1. **Backup the Original Script**

   ```bash
   cp $LEJOS_NXJ/bin/nxj $LEJOS_NXJ/bin/nxj.bak
   ```

2. **Edit the Script**

   ```bash
   nano $LEJOS_NXJ/bin/nxj
   ```

3. **Update the Platform-Specific Section**

   Locate the following block:

   ```bash
   case $(uname -s) in
       CYGWIN*) SEP=";";;
       Darwin) NXJ_FORCE32="-d32";;
   esac
   ```

   Change it to:

   ```bash
   case $(uname -s) in
       CYGWIN*) SEP=";";;
       Darwin) NXJ_FORCE32="";;
   esac
   ```

4. **Save and Exit**  
   (For Nano, press `Ctrl+O`, then `Enter`, and finally `Ctrl+X`.)

## 5. Test the Setup

Run the following command:

```bash
nxj
```

You should now see usage or help information from leJOS NXJ without the 32‑bit error.