package nadav.altabet.nlb;

import java.util.Calendar;

public class Message {
    private String messageUID;
    private String senderEmail;
    private String receiverEmail;
    private String messageContent;
    private String messageHeadline;
    private Date sentOnDate;
    private String status; //t=read | f=unread

    public Message(String messageUID, String receiverEmail, String messageContent, String messageHeadline) {
        this.messageUID = messageUID;
        this.senderEmail = Client.getCurrentUser().getEmail();
        this.receiverEmail = receiverEmail;
        this.messageContent = messageContent;
        this.messageHeadline = messageHeadline;
        Calendar calendar = Calendar.getInstance();
        this.sentOnDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        this.status = "false";
    }

    public Message() {
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

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageHeadline() {
        return messageHeadline;
    }

    public void setMessageHeadline(String messageHeadline) {
        this.messageHeadline = messageHeadline;
    }

    public Date getSentOnDate() {
        return sentOnDate;
    }

    public void setSentOnDate(Date sentOnDate) {
        this.sentOnDate = sentOnDate;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
