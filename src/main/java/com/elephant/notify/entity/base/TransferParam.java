package com.elephant.notify.entity.base;
import java.util.List;

/**
 * 
 * @author clark
 * May 23, 2018 10:48:16 AM
 */
public class TransferParam {
	
	private Double totalAmt;
	private String senderAddress;
	private String senderPrivateKey;
	private List<String> addrList;
	private List<Double> amtList;
	
	public List<String> getAddrList() {
		return addrList;
	}
	public void setAddrList(List<String> addrList) {
		this.addrList = addrList;
	}
	public List<Double> getAmtList() {
		return amtList;
	}
	public void setAmtList(List<Double> amtList) {
		this.amtList = amtList;
	}
	private String memo;
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSenderPrivateKey() {
		return senderPrivateKey;
	}
	public void setSenderPrivateKey(String senderPrivateKey) {
		this.senderPrivateKey = senderPrivateKey;
	}
	
}
