package com.dnlab.jiny.highfreq;

public class ConvertAscii {

    private String inputMessage;
    private byte[] ascii;
    private StringBuffer code;
    // private byte alpha_lower = 97; // in ascii 'a-z' is 97-122

    public ConvertAscii(String message){
        this.inputMessage = message;
        ascii = new byte[inputMessage.length()];
        code = new StringBuffer();
        StringtoCode();
    }

    private void StringtoCode(){
        for(int i=0; i < inputMessage.length(); i++){
            ascii[i] = (byte) inputMessage.charAt(i);
            /*ascii[i] -= alpha_lower;
            int ascii_length = Integer.toBinaryString(ascii[i]).length();
            if(ascii_length < 5){
                for(int j=0; j < (5 - ascii_length); i++){
                    code.append(0);
                }
            }*/
            code.append(Integer.toBinaryString(ascii[i]));
        }
    }

    public String getCode(){
        return code.toString();
    }

    public String getInputMessage(){
        return inputMessage;
    }

}
