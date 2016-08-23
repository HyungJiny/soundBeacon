package com.dnlab.jiny.highfreq;

public class ConvertAscii {

    private String inputMessage;
    private byte[] ascii;
    private StringBuffer code;

    public ConvertAscii(String message){
        this.inputMessage = message;
        ascii = new byte[inputMessage.length()];
        code = new StringBuffer();
        StringtoCode();
    }

    private void StringtoCode(){
        for(int i=0; i < inputMessage.length(); i++){
            ascii[i] = (byte) inputMessage.charAt(i);
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
