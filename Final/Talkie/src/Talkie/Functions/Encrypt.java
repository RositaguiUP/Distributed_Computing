package Talkie.Functions;

public class Encrypt {
    public static String caesarEncrypt(int shift, String plaintext) {
        StringBuilder encryptedText = new StringBuilder();
        
        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            
            if (Character.isLetter(c) || Character.isDigit(c)) {
                int base;
                if (Character.isLowerCase(c)) {
                    base = 'a';
                } else if (Character.isUpperCase(c)) {
                    base = 'A';
                } else {
                    if((int)c>52){
                        encryptedText.append((char)(c-5));
                    }else{
                        encryptedText.append((char)(c+5));
                    }
                    continue;
                }
                c = (char)(((int)c - base + shift) % 26 + base);
            }
            
            encryptedText.append(c);
        }
        
        return encryptedText.toString();
    }

    public static String caesarDecrypt(int shift, String ciphertext) {
        StringBuilder decryptedText = new StringBuilder();
        
        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            
            if (Character.isLetter(c) || Character.isDigit(c)) {
                int base;
                if (Character.isLowerCase(c)) {
                    base = 'a';
                } else if (Character.isUpperCase(c)) {
                    base = 'A';
                } else {
                    if((int)c>52){
                        decryptedText.append((char)(c-5));
                    }else{
                        decryptedText.append((char)(c+5));
                    }
                    continue;
                }
                c = (char)(((int)c - base - shift + 26) % 26 + base);
            }
            
            decryptedText.append(c);
        }
        
        return decryptedText.toString();
    }
}
