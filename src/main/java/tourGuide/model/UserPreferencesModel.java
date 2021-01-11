package tourGuide.model;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import tourGuide.dto.UserPreferencesDTO;

public class UserPreferencesModel {

	private int attractionProximity = Integer.MAX_VALUE;
	private CurrencyUnit currency = Monetary.getCurrency("USD");
	private Money lowerPricePoint = Money.of(0, currency);
	private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
	private int tripDuration = 1;
	private int ticketQuantity = 1;
	private int numberOfAdults = 1;
	private int numberOfChildren = 0;
	
	public UserPreferencesModel() {
	}

	public UserPreferencesModel(UserPreferencesDTO userPreferencesDTO) {
		this.attractionProximity = userPreferencesDTO.getAttractionProximity();
		this.currency = Monetary.getCurrency(userPreferencesDTO.getCurrency());
		this.lowerPricePoint = Money.of(userPreferencesDTO.getLowerPricePoint(), currency);
		this.highPricePoint = Money.of(userPreferencesDTO.getHighPricePoint(), currency);
		this.tripDuration = userPreferencesDTO.getTripDuration();
		this.ticketQuantity = userPreferencesDTO.getTicketQuantity();
		this.numberOfAdults = userPreferencesDTO.getNumberOfAdults();
		this.numberOfChildren = userPreferencesDTO.getNumberOfChildren();
	}

	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}
	
	public int getAttractionProximity() {
		return attractionProximity;
	}

	public CurrencyUnit getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyUnit currency) {
		this.currency = currency;
	}
	
	public Money getLowerPricePoint() {
		return lowerPricePoint;
	}

	public void setLowerPricePoint(Money lowerPricePoint) {
		this.lowerPricePoint = lowerPricePoint;
	}

	public Money getHighPricePoint() {
		return highPricePoint;
	}

	public void setHighPricePoint(Money highPricePoint) {
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