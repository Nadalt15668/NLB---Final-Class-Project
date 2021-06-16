package nadav.altabet.nlb;

import java.util.Calendar;

public class Message {
    private String messageUID;
    private String senderFullName;
    private String receiverFullName;
    private String messageContent;
    private String messageHeadline;
    private Date sentOnDate;
    private String status; //t=read | f=unread

    public Message(String messageUID, String receiverFullName, String messageContent, String messageHeadline) {
        this.messageUID = messageUID;
        this.senderFullName = Client.getCurrentUser().getFirst_name() + " " + Client.getCurrentUser().getLast_name();
        this.receiverFullName = receiverFullName;
        this.messageContent = messageContent;
        this.messageHeadline = messageHeadline;
        Calendar calendar = Calendar.getInstance();
        this.sentOnDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        this.status = "unread";
    }

    public Message() {
    }

    public String getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(String messageUID) {
        this.messageUID = messageUID;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
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
