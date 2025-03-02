package com.cheqin.booking.models.confirmbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_request_id")
    @Expose
    private Integer userRequestId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("offer_price")
    @Expose
    private Integer offerPrice;
    @SerializedName("offered_time")
    @Expose
    private String offeredTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("item_info")
    @Expose
    private String itemInfo;
    @SerializedName("minimum_offer_time")
    @Expose
    private Integer minimumOfferTime;
    @SerializedName("offer_expired")
    @Expose
    private String offerExpired;
    @SerializedName("shipping_charge")
    @Expose
    private Object shippingCharge;
    @SerializedName("mode_of_shipping")
    @Expose
    private Object modeOfShipping;
    @SerializedName("shipment_days")
    @Expose
    private Object shipmentDays;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("company_name")
    @Expose
    private Object companyName;
    @SerializedName("contact_person")
    @Expose
    private Object contactPerson;
    @SerializedName("contact_number")
    @Expose
    private Object contactNumber;
    @SerializedName("sms_flag")
    @Expose
    private Boolean smsFlag;
    @SerializedName("reject_offer")
    @Expose
    private Boolean rejectOffer;
    @SerializedName("fee_agreed")
    @Expose
    private Object feeAgreed;
    @SerializedName("buysell_img_file_name")
    @Expose
    private Object buysellImgFileName;
    @SerializedName("buysell_img_content_type")
    @Expose
    private Object buysellImgContentType;
    @SerializedName("buysell_img_file_size")
    @Expose
    private Object buysellImgFileSize;
    @SerializedName("buysell_img_updated_at")
    @Expose
    private Object buysellImgUpdatedAt;
    @SerializedName("flight_leaving_depart_city")
    @Expose
    private Object flightLeavingDepartCity;
    @SerializedName("flight_leaving_depart_state")
    @Expose
    private Object flightLeavingDepartState;
    @SerializedName("flight_leaving_depart_country")
    @Expose
    private Object flightLeavingDepartCountry;
    @SerializedName("flight_leaving_arrival_city")
    @Expose
    private Object flightLeavingArrivalCity;
    @SerializedName("flight_leaving_arrival_state")
    @Expose
    private Object flightLeavingArrivalState;
    @SerializedName("flight_leaving_arrival_country")
    @Expose
    private Object flightLeavingArrivalCountry;
    @SerializedName("flight_leaving_departure")
    @Expose
    private Object flightLeavingDeparture;
    @SerializedName("flight_leaving_arrival")
    @Expose
    private Object flightLeavingArrival;
    @SerializedName("flight_leaving_stops")
    @Expose
    private Integer flightLeavingStops;
    @SerializedName("flight_leaving_airline")
    @Expose
    private Object flightLeavingAirline;
    @SerializedName("flight_leaving_class")
    @Expose
    private Object flightLeavingClass;
    @SerializedName("flight_leaving_otherslot")
    @Expose
    private Object flightLeavingOtherslot;
    @SerializedName("flight_return_depart_city")
    @Expose
    private Object flightReturnDepartCity;
    @SerializedName("flight_return_depart_state")
    @Expose
    private Object flightReturnDepartState;
    @SerializedName("flight_return_depart_country")
    @Expose
    private Object flightReturnDepartCountry;
    @SerializedName("flight_return_arrival_city")
    @Expose
    private Object flightReturnArrivalCity;
    @SerializedName("flight_return_arrival_state")
    @Expose
    private Object flightReturnArrivalState;
    @SerializedName("flight_return_arrival_country")
    @Expose
    private Object flightReturnArrivalCountry;
    @SerializedName("flight_returning_departure")
    @Expose
    private Object flightReturningDeparture;
    @SerializedName("flight_returning_arrival")
    @Expose
    private Object flightReturningArrival;
    @SerializedName("flight_returning_stops")
    @Expose
    private Integer flightReturningStops;
    @SerializedName("flight_returning_airline")
    @Expose
    private Object flightReturningAirline;
    @SerializedName("flight_returning_class")
    @Expose
    private Object flightReturningClass;
    @SerializedName("flight_returning_otherslot")
    @Expose
    private Object flightReturningOtherslot;
    @SerializedName("is_template")
    @Expose
    private Integer isTemplate;
    @SerializedName("tax_percent")
    @Expose
    private Integer taxPercent;
    @SerializedName("isbn_code")
    @Expose
    private Object isbnCode;
    @SerializedName("publisher")
    @Expose
    private Object publisher;
    @SerializedName("condition")
    @Expose
    private Object condition;
    @SerializedName("mrp")
    @Expose
    private Object mrp;
    @SerializedName("library_id")
    @Expose
    private Object libraryId;
    @SerializedName("is_cust_support_esclation")
    @Expose
    private Integer isCustSupportEsclation;
    @SerializedName("is_extended_for")
    @Expose
    private Integer isExtendedFor;
    @SerializedName("offer_img_root")
    @Expose
    private String offerImgRoot;
    @SerializedName("tenure_term")
    @Expose
    private Object tenureTerm;
    @SerializedName("interest_rate")
    @Expose
    private Object interestRate;
    @SerializedName("warranty")
    @Expose
    private Object warranty;
    @SerializedName("warranty_tenure")
    @Expose
    private Object warrantyTenure;
    @SerializedName("cab_max_km")
    @Expose
    private Object cabMaxKm;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("exchange_rate")
    @Expose
    private Integer exchangeRate;
    @SerializedName("total_price_inr")
    @Expose
    private Integer totalPriceInr;
    @SerializedName("total_price_usd")
    @Expose
    private Integer totalPriceUsd;
    @SerializedName("onbehalf_hotel_name")
    @Expose
    private String onbehalfHotelName;
    @SerializedName("onbehalf_hotel_addr")
    @Expose
    private String onbehalfHotelAddr;
    @SerializedName("onbehalf_hotel_contact")
    @Expose
    private String onbehalfHotelContact;
    @SerializedName("flight_leaving_depart_latilong")
    @Expose
    private Object flightLeavingDepartLatilong;
    @SerializedName("flight_leaving_arrival_latilong")
    @Expose
    private Object flightLeavingArrivalLatilong;
    @SerializedName("flight_return_depart_latilong")
    @Expose
    private Object flightReturnDepartLatilong;
    @SerializedName("flight_return_arrival_latilong")
    @Expose
    private Object flightReturnArrivalLatilong;
    @SerializedName("color_pref")
    @Expose
    private Object colorPref;
    @SerializedName("make_year")
    @Expose
    private Object makeYear;
    @SerializedName("pbc_interest")
    @Expose
    private Integer pbcInterest;
    @SerializedName("rank_state")
    @Expose
    private String rankState;
    @SerializedName("hotel_latitude")
    @Expose
    private String hotelLatitude;
    @SerializedName("hotel_longitude")
    @Expose
    private String hotelLongitude;
    @SerializedName("seller_cur_latilong")
    @Expose
    private Object sellerCurLatilong;
    @SerializedName("min_price")
    @Expose
    private Integer minPrice;
    @SerializedName("min_total_price")
    @Expose
    private Integer minTotalPrice;
    @SerializedName("offer_price_inr")
    @Expose
    private Object offerPriceInr;
    @SerializedName("offer_price_usd")
    @Expose
    private Object offerPriceUsd;
    @SerializedName("has_buyer_paid")
    @Expose
    private Integer hasBuyerPaid;
    @SerializedName("required_admin_approval")
    @Expose
    private Integer requiredAdminApproval;
    @SerializedName("admin_approval_rejection_reason")
    @Expose
    private Object adminApprovalRejectionReason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(Integer userRequestId) {
        this.userRequestId = userRequestId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Integer offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferedTime() {
        return offeredTime;
    }

    public void setOfferedTime(String offeredTime) {
        this.offeredTime = offeredTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public Integer getMinimumOfferTime() {
        return minimumOfferTime;
    }

    public void setMinimumOfferTime(Integer minimumOfferTime) {
        this.minimumOfferTime = minimumOfferTime;
    }

    public String getOfferExpired() {
        return offerExpired;
    }

    public void setOfferExpired(String offerExpired) {
        this.offerExpired = offerExpired;
    }

    public Object getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(Object shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public Object getModeOfShipping() {
        return modeOfShipping;
    }

    public void setModeOfShipping(Object modeOfShipping) {
        this.modeOfShipping = modeOfShipping;
    }

    public Object getShipmentDays() {
        return shipmentDays;
    }

    public void setShipmentDays(Object shipmentDays) {
        this.shipmentDays = shipmentDays;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Object contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Object getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Object contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Boolean getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(Boolean smsFlag) {
        this.smsFlag = smsFlag;
    }

    public Boolean getRejectOffer() {
        return rejectOffer;
    }

    public void setRejectOffer(Boolean rejectOffer) {
        this.rejectOffer = rejectOffer;
    }

    public Object getFeeAgreed() {
        return feeAgreed;
    }

    public void setFeeAgreed(Object feeAgreed) {
        this.feeAgreed = feeAgreed;
    }

    public Object getBuysellImgFileName() {
        return buysellImgFileName;
    }

    public void setBuysellImgFileName(Object buysellImgFileName) {
        this.buysellImgFileName = buysellImgFileName;
    }

    public Object getBuysellImgContentType() {
        return buysellImgContentType;
    }

    public void setBuysellImgContentType(Object buysellImgContentType) {
        this.buysellImgContentType = buysellImgContentType;
    }

    public Object getBuysellImgFileSize() {
        return buysellImgFileSize;
    }

    public void setBuysellImgFileSize(Object buysellImgFileSize) {
        this.buysellImgFileSize = buysellImgFileSize;
    }

    public Object getBuysellImgUpdatedAt() {
        return buysellImgUpdatedAt;
    }

    public void setBuysellImgUpdatedAt(Object buysellImgUpdatedAt) {
        this.buysellImgUpdatedAt = buysellImgUpdatedAt;
    }

    public Object getFlightLeavingDepartCity() {
        return flightLeavingDepartCity;
    }

    public void setFlightLeavingDepartCity(Object flightLeavingDepartCity) {
        this.flightLeavingDepartCity = flightLeavingDepartCity;
    }

    public Object getFlightLeavingDepartState() {
        return flightLeavingDepartState;
    }

    public void setFlightLeavingDepartState(Object flightLeavingDepartState) {
        this.flightLeavingDepartState = flightLeavingDepartState;
    }

    public Object getFlightLeavingDepartCountry() {
        return flightLeavingDepartCountry;
    }

    public void setFlightLeavingDepartCountry(Object flightLeavingDepartCountry) {
        this.flightLeavingDepartCountry = flightLeavingDepartCountry;
    }

    public Object getFlightLeavingArrivalCity() {
        return flightLeavingArrivalCity;
    }

    public void setFlightLeavingArrivalCity(Object flightLeavingArrivalCity) {
        this.flightLeavingArrivalCity = flightLeavingArrivalCity;
    }

    public Object getFlightLeavingArrivalState() {
        return flightLeavingArrivalState;
    }

    public void setFlightLeavingArrivalState(Object flightLeavingArrivalState) {
        this.flightLeavingArrivalState = flightLeavingArrivalState;
    }

    public Object getFlightLeavingArrivalCountry() {
        return flightLeavingArrivalCountry;
    }

    public void setFlightLeavingArrivalCountry(Object flightLeavingArrivalCountry) {
        this.flightLeavingArrivalCountry = flightLeavingArrivalCountry;
    }

    public Object getFlightLeavingDeparture() {
        return flightLeavingDeparture;
    }

    public void setFlightLeavingDeparture(Object flightLeavingDeparture) {
        this.flightLeavingDeparture = flightLeavingDeparture;
    }

    public Object getFlightLeavingArrival() {
        return flightLeavingArrival;
    }

    public void setFlightLeavingArrival(Object flightLeavingArrival) {
        this.flightLeavingArrival = flightLeavingArrival;
    }

    public Integer getFlightLeavingStops() {
        return flightLeavingStops;
    }

    public void setFlightLeavingStops(Integer flightLeavingStops) {
        this.flightLeavingStops = flightLeavingStops;
    }

    public Object getFlightLeavingAirline() {
        return flightLeavingAirline;
    }

    public void setFlightLeavingAirline(Object flightLeavingAirline) {
        this.flightLeavingAirline = flightLeavingAirline;
    }

    public Object getFlightLeavingClass() {
        return flightLeavingClass;
    }

    public void setFlightLeavingClass(Object flightLeavingClass) {
        this.flightLeavingClass = flightLeavingClass;
    }

    public Object getFlightLeavingOtherslot() {
        return flightLeavingOtherslot;
    }

    public void setFlightLeavingOtherslot(Object flightLeavingOtherslot) {
        this.flightLeavingOtherslot = flightLeavingOtherslot;
    }

    public Object getFlightReturnDepartCity() {
        return flightReturnDepartCity;
    }

    public void setFlightReturnDepartCity(Object flightReturnDepartCity) {
        this.flightReturnDepartCity = flightReturnDepartCity;
    }

    public Object getFlightReturnDepartState() {
        return flightReturnDepartState;
    }

    public void setFlightReturnDepartState(Object flightReturnDepartState) {
        this.flightReturnDepartState = flightReturnDepartState;
    }

    public Object getFlightReturnDepartCountry() {
        return flightReturnDepartCountry;
    }

    public void setFlightReturnDepartCountry(Object flightReturnDepartCountry) {
        this.flightReturnDepartCountry = flightReturnDepartCountry;
    }

    public Object getFlightReturnArrivalCity() {
        return flightReturnArrivalCity;
    }

    public void setFlightReturnArrivalCity(Object flightReturnArrivalCity) {
        this.flightReturnArrivalCity = flightReturnArrivalCity;
    }

    public Object getFlightReturnArrivalState() {
        return flightReturnArrivalState;
    }

    public void setFlightReturnArrivalState(Object flightReturnArrivalState) {
        this.flightReturnArrivalState = flightReturnArrivalState;
    }

    public Object getFlightReturnArrivalCountry() {
        return flightReturnArrivalCountry;
    }

    public void setFlightReturnArrivalCountry(Object flightReturnArrivalCountry) {
        this.flightReturnArrivalCountry = flightReturnArrivalCountry;
    }

    public Object getFlightReturningDeparture() {
        return flightReturningDeparture;
    }

    public void setFlightReturningDeparture(Object flightReturningDeparture) {
        this.flightReturningDeparture = flightReturningDeparture;
    }

    public Object getFlightReturningArrival() {
        return flightReturningArrival;
    }

    public void setFlightReturningArrival(Object flightReturningArrival) {
        this.flightReturningArrival = flightReturningArrival;
    }

    public Integer getFlightReturningStops() {
        return flightReturningStops;
    }

    public void setFlightReturningStops(Integer flightReturningStops) {
        this.flightReturningStops = flightReturningStops;
    }

    public Object getFlightReturningAirline() {
        return flightReturningAirline;
    }

    public void setFlightReturningAirline(Object flightReturningAirline) {
        this.flightReturningAirline = flightReturningAirline;
    }

    public Object getFlightReturningClass() {
        return flightReturningClass;
    }

    public void setFlightReturningClass(Object flightReturningClass) {
        this.flightReturningClass = flightReturningClass;
    }

    public Object getFlightReturningOtherslot() {
        return flightReturningOtherslot;
    }

    public void setFlightReturningOtherslot(Object flightReturningOtherslot) {
        this.flightReturningOtherslot = flightReturningOtherslot;
    }

    public Integer getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Integer isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Integer getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(Integer taxPercent) {
        this.taxPercent = taxPercent;
    }

    public Object getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(Object isbnCode) {
        this.isbnCode = isbnCode;
    }

    public Object getPublisher() {
        return publisher;
    }

    public void setPublisher(Object publisher) {
        this.publisher = publisher;
    }

    public Object getCondition() {
        return condition;
    }

    public void setCondition(Object condition) {
        this.condition = condition;
    }

    public Object getMrp() {
        return mrp;
    }

    public void setMrp(Object mrp) {
        this.mrp = mrp;
    }

    public Object getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Object libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getIsCustSupportEsclation() {
        return isCustSupportEsclation;
    }

    public void setIsCustSupportEsclation(Integer isCustSupportEsclation) {
        this.isCustSupportEsclation = isCustSupportEsclation;
    }

    public Integer getIsExtendedFor() {
        return isExtendedFor;
    }

    public void setIsExtendedFor(Integer isExtendedFor) {
        this.isExtendedFor = isExtendedFor;
    }

    public String getOfferImgRoot() {
        return offerImgRoot;
    }

    public void setOfferImgRoot(String offerImgRoot) {
        this.offerImgRoot = offerImgRoot;
    }

    public Object getTenureTerm() {
        return tenureTerm;
    }

    public void setTenureTerm(Object tenureTerm) {
        this.tenureTerm = tenureTerm;
    }

    public Object getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Object interestRate) {
        this.interestRate = interestRate;
    }

    public Object getWarranty() {
        return warranty;
    }

    public void setWarranty(Object warranty) {
        this.warranty = warranty;
    }

    public Object getWarrantyTenure() {
        return warrantyTenure;
    }

    public void setWarrantyTenure(Object warrantyTenure) {
        this.warrantyTenure = warrantyTenure;
    }

    public Object getCabMaxKm() {
        return cabMaxKm;
    }

    public void setCabMaxKm(Object cabMaxKm) {
        this.cabMaxKm = cabMaxKm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getTotalPriceInr() {
        return totalPriceInr;
    }

    public void setTotalPriceInr(Integer totalPriceInr) {
        this.totalPriceInr = totalPriceInr;
    }

    public Integer getTotalPriceUsd() {
        return totalPriceUsd;
    }

    public void setTotalPriceUsd(Integer totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    public String getOnbehalfHotelName() {
        return onbehalfHotelName;
    }

    public void setOnbehalfHotelName(String onbehalfHotelName) {
        this.onbehalfHotelName = onbehalfHotelName;
    }

    public String getOnbehalfHotelAddr() {
        return onbehalfHotelAddr;
    }

    public void setOnbehalfHotelAddr(String onbehalfHotelAddr) {
        this.onbehalfHotelAddr = onbehalfHotelAddr;
    }

    public String getOnbehalfHotelContact() {
        return onbehalfHotelContact;
    }

    public void setOnbehalfHotelContact(String onbehalfHotelContact) {
        this.onbehalfHotelContact = onbehalfHotelContact;
    }

    public Object getFlightLeavingDepartLatilong() {
        return flightLeavingDepartLatilong;
    }

    public void setFlightLeavingDepartLatilong(Object flightLeavingDepartLatilong) {
        this.flightLeavingDepartLatilong = flightLeavingDepartLatilong;
    }

    public Object getFlightLeavingArrivalLatilong() {
        return flightLeavingArrivalLatilong;
    }

    public void setFlightLeavingArrivalLatilong(Object flightLeavingArrivalLatilong) {
        this.flightLeavingArrivalLatilong = flightLeavingArrivalLatilong;
    }

    public Object getFlightReturnDepartLatilong() {
        return flightReturnDepartLatilong;
    }

    public void setFlightReturnDepartLatilong(Object flightReturnDepartLatilong) {
        this.flightReturnDepartLatilong = flightReturnDepartLatilong;
    }

    public Object getFlightReturnArrivalLatilong() {
        return flightReturnArrivalLatilong;
    }

    public void setFlightReturnArrivalLatilong(Object flightReturnArrivalLatilong) {
        this.flightReturnArrivalLatilong = flightReturnArrivalLatilong;
    }

    public Object getColorPref() {
        return colorPref;
    }

    public void setColorPref(Object colorPref) {
        this.colorPref = colorPref;
    }

    public Object getMakeYear() {
        return makeYear;
    }

    public void setMakeYear(Object makeYear) {
        this.makeYear = makeYear;
    }

    public Integer getPbcInterest() {
        return pbcInterest;
    }

    public void setPbcInterest(Integer pbcInterest) {
        this.pbcInterest = pbcInterest;
    }

    public String getRankState() {
        return rankState;
    }

    public void setRankState(String rankState) {
        this.rankState = rankState;
    }

    public String getHotelLatitude() {
        return hotelLatitude;
    }

    public void setHotelLatitude(String hotelLatitude) {
        this.hotelLatitude = hotelLatitude;
    }

    public String getHotelLongitude() {
        return hotelLongitude;
    }

    public void setHotelLongitude(String hotelLongitude) {
        this.hotelLongitude = hotelLongitude;
    }

    public Object getSellerCurLatilong() {
        return sellerCurLatilong;
    }

    public void setSellerCurLatilong(Object sellerCurLatilong) {
        this.sellerCurLatilong = sellerCurLatilong;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMinTotalPrice() {
        return minTotalPrice;
    }

    public void setMinTotalPrice(Integer minTotalPrice) {
        this.minTotalPrice = minTotalPrice;
    }

    public Object getOfferPriceInr() {
        return offerPriceInr;
    }

    public void setOfferPriceInr(Object offerPriceInr) {
        this.offerPriceInr = offerPriceInr;
    }

    public Object getOfferPriceUsd() {
        return offerPriceUsd;
    }

    public void setOfferPriceUsd(Object offerPriceUsd) {
        this.offerPriceUsd = offerPriceUsd;
    }

    public Integer getHasBuyerPaid() {
        return hasBuyerPaid;
    }

    public void setHasBuyerPaid(Integer hasBuyerPaid) {
        this.hasBuyerPaid = hasBuyerPaid;
    }

    public Integer getRequiredAdminApproval() {
        return requiredAdminApproval;
    }

    public void setRequiredAdminApproval(Integer requiredAdminApproval) {
        this.requiredAdminApproval = requiredAdminApproval;
    }

    public Object getAdminApprovalRejectionReason() {
        return adminApprovalRejectionReason;
    }

    public void setAdminApprovalRejectionReason(Object adminApprovalRejectionReason) {
        this.adminApprovalRejectionReason = adminApprovalRejectionReason;
    }

}
