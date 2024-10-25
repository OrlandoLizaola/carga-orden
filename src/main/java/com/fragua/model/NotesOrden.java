package com.fragua.model;

public class NotesOrden {
    
    private String ReasonCode;
    private String SequenceNo;
    private String NoteText;

    public NotesOrden(String ReasonCode, String SequenceNo, String NoteText) {
        this.setReasonCode(ReasonCode);
        this.setSequenceNo(SequenceNo);
        this.setNoteText(NoteText);
        
    }

	public String getReasonCode() {
		return ReasonCode;
	}

	public void setReasonCode(String reasonCode) {
		ReasonCode = reasonCode;
	}

	public String getSequenceNo() {
		return SequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		SequenceNo = sequenceNo;
	}

	public String getNoteText() {
		return NoteText;
	}

	public void setNoteText(String noteText) {
		NoteText = noteText;
	}
    
}