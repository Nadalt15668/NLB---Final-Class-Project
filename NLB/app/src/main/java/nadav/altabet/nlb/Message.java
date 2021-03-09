package nadav.altabet.nlb;

public class Message {
    private String messageUID;
    private String senderEmail;
    private String RecieverUID;
    private String messageContent;
    private boolean status; //t=read

    public Message(String messageUID, String recieverUID, String messageContent) {
        this.messageUID = messageUID;
        this.senderEmail = Client.getCurrentUser().getEmail();
        RecieverUID = recieverUID;
        this.messageContent = messageContent;
        this.status = false;
    }

    public String getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(String messageUID) {
        this.messageUID = messageUID;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecieverUID() {
        return RecieverUID;
    }

    public void setRecieverUID(String recieverUID) {
        RecieverUID = recieverUID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
