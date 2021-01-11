package tourGuide.dto;

import tourGuide.model.UserPreferencesModel;

public class UserPreferencesDTO {

	private String username;
	private int attractionProximity;
	private String currency;
	private int lowerPricePoint;
	private int highPricePoint;
	private int tripDuration;
	private int ticketQuantity;
	private int numberOfAdults;
	private int numberOfChildren;

	public UserPreferencesDTO() {
	}

	public UserPreferencesDTO(String username, int attractionProximity, String currency, int lowerPricePoint,
							  int highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults,
							  int numberOfChildren) {
		this.username = username;
		this.attractionProximity = attractionProximity;
		this.currency = currency;
		this.lowerPricePoint = lowerPricePoint;
		this.highPricePoint = highPricePoint;
		this.tripDuration = tripDuration;
		this.ticketQuantity = ticketQuantity;
		this.numberOfAdults = numberOfAdults;
		this.numberOfChildren = numberOfChildren;
	}

	public UserPreferencesDTO(String username, UserPreferencesModel userPreferencesModel) {
		this.username = username;
		this.currency = userPreferencesModel.getCurrency().toString();
		this.attractionProximity = userPreferencesModel.getAttractionProximity();
		this.lowerPricePoint = userPreferencesModel.getLowerPricePoint().getNumber().intValueExact();
		this.highPricePoint = userPreferencesModel.getHighPricePoint().getNumber().intValueExact();
		this.tripDuration = userPreferencesModel.getTripDuration();
		this.ticketQuantity = userPreferencesModel.getTicketQuantity();
		this.numberOfAdults = userPreferencesModel.getNumberOfAdults();
		this.numberOfChildren = userPreferencesModel.getNumberOfChildren();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAttractionProximity() {
		return attractionProximity;
	}

	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getLowerPricePoint() {
		return lowerPricePoint;
	}

	public void setLowerPricePoint(int lowerPricePoint) {
		this.lowerPricePoint = lowerPricePoint;
	}

	public int getHighPricePoint() {
		return highPricePoint;
	}

	public void setHighPricePoint(int highPricePoint) {
		this.highPricePoint = highPricePoint;
	}

	public int getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(int tripDuration) {
		this.tripDuration = tripDuration;
	}

	public int getTicketQuantity() {
		return ticketQuantity;
	}

	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
}