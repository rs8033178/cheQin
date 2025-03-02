
package com.cheqin.booking.mappager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("buysell_category_id")
    @Expose
    private Integer buysellCategoryId;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("zipcode")
    @Expose
    private Object zipcode;
    @SerializedName("radius")
    @Expose
    private Object radius;
    @SerializedName("descision_factor")
    @Expose
    private String descisionFactor;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("location_id")
    @Expose
    private Object locationId;
    @SerializedName("item_info")
    @Expose
    private String itemInfo;
    @SerializedName("item_condition")
    @Expose
    private Object itemCondition;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("number_of_items")
    @Expose
    private Object numberOfItems;
    @SerializedName("req_total_volume")
    @Expose
    private Integer reqTotalVolume;
    @SerializedName("total_price")
    @Expose
    private Object totalPrice;
    @SerializedName("pick_up_date")
    @Expose
    private Object pickUpDate;
    @SerializedName("drop_off_date")
    @Expose
    private Object dropOffDate;
    @SerializedName("check_in_date")
    @Expose
    private String checkInDate;
    @SerializedName("check_out_date")
    @Expose
    private String checkOutDate;
    @SerializedName("no_of_rooms")
    @Expose
    private Integer noOfRooms;
    @SerializedName("adult")
    @Expose
    private Integer adult;
    @SerializedName("children")
    @Expose
    private Integer children;
    @SerializedName("no_of_bed_rooms")
    @Expose
    private Object noOfBedRooms;
    @SerializedName("sq_feet")
    @Expose
    private Object sqFeet;
    @SerializedName("from_date")
    @Expose
    private Object fromDate;
    @SerializedName("to_date")
    @Expose
    private Object toDate;
    @SerializedName("item_code")
    @Expose
    private Object itemCode;
    @SerializedName("item_spec")
    @Expose
    private Object itemSpec;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("expiry_info")
    @Expose
    private Boolean expiryInfo;
    @SerializedName("seller_expiry_info")
    @Expose
    private Boolean sellerExpiryInfo;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("flight_type")
    @Expose
    private Object flightType;
    @SerializedName("travel_type")
    @Expose
    private Object travelType;
    @SerializedName("flight_depart_city")
    @Expose
    private Object flightDepartCity;
    @SerializedName("flight_depart_state")
    @Expose
    private Object flightDepartState;
    @SerializedName("flight_depart_country")
    @Expose
    private Object flightDepartCountry;
    @SerializedName("flight_arrival_city")
    @Expose
    private Object flightArrivalCity;
    @SerializedName("flight_arrival_state")
    @Expose
    private Object flightArrivalState;
    @SerializedName("flight_arrival_country")
    @Expose
    private Object flightArrivalCountry;
    @SerializedName("flight_adults")
    @Expose
    private Integer flightAdults;
    @SerializedName("flight_children")
    @Expose
    private Integer flightChildren;
    @SerializedName("flight_infants")
    @Expose
    private Integer flightInfants;
    @SerializedName("flight_seniors")
    @Expose
    private Integer flightSeniors;
    @SerializedName("flight_orgin_datetime")
    @Expose
    private Object flightOrginDatetime;
    @SerializedName("flight_return_datetime")
    @Expose
    private Object flightReturnDatetime;
    @SerializedName("flight_no_stops")
    @Expose
    private Integer flightNoStops;
    @SerializedName("flight_preferred_airlines")
    @Expose
    private Object flightPreferredAirlines;
    @SerializedName("flight_preferred_class")
    @Expose
    private Object flightPreferredClass;
    @SerializedName("flight_preferred_timeslot")
    @Expose
    private Object flightPreferredTimeslot;
    @SerializedName("grand_total")
    @Expose
    private Integer grandTotal;
    @SerializedName("book_cover_url")
    @Expose
    private Object bookCoverUrl;
    @SerializedName("book_authors")
    @Expose
    private Object bookAuthors;
    @SerializedName("book_publisher")
    @Expose
    private Object bookPublisher;
    @SerializedName("is_extended")
    @Expose
    private Integer isExtended;
    @SerializedName("is_cust_support_esclation")
    @Expose
    private Integer isCustSupportEsclation;
    @SerializedName("color_pref")
    @Expose
    private Object colorPref;
    @SerializedName("make_year")
    @Expose
    private Object makeYear;
    @SerializedName("contact_pref")
    @Expose
    private Object contactPref;
    @SerializedName("item_catalog_id")
    @Expose
    private Object itemCatalogId;
    @SerializedName("tenure_term")
    @Expose
    private Object tenureTerm;
    @SerializedName("interest_rate")
    @Expose
    private Object interestRate;
    @SerializedName("is_B2B")
    @Expose
    private Integer isB2B;
    @SerializedName("cab_starting_point")
    @Expose
    private Object cabStartingPoint;
    @SerializedName("cab_dropping_point")
    @Expose
    private Object cabDroppingPoint;
    @SerializedName("landmark")
    @Expose
    private Object landmark;
    @SerializedName("cab_start_datetime")
    @Expose
    private Object cabStartDatetime;
    @SerializedName("cab_bags")
    @Expose
    private Object cabBags;
    @SerializedName("type_of_vehicle")
    @Expose
    private Object typeOfVehicle;
    @SerializedName("ac_non_ac")
    @Expose
    private Object acNonAc;
    @SerializedName("cab_no_of_days")
    @Expose
    private Object cabNoOfDays;
    @SerializedName("cab_trip_details")
    @Expose
    private Object cabTripDetails;
    @SerializedName("cab_booking_time")
    @Expose
    private Object cabBookingTime;
    @SerializedName("cab_end_datetime")
    @Expose
    private Object cabEndDatetime;
    @SerializedName("crm_opportinuty_id")
    @Expose
    private Object crmOpportinutyId;
    @SerializedName("crm_revenue_id")
    @Expose
    private Object crmRevenueId;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("exchange_rate")
    @Expose
    private Integer exchangeRate;
    @SerializedName("latilong")
    @Expose
    private String latilong;
    @SerializedName("flight_depart_latilong")
    @Expose
    private Object flightDepartLatilong;
    @SerializedName("flight_arrival_latilong")
    @Expose
    private Object flightArrivalLatilong;
    @SerializedName("item_unit")
    @Expose
    private Object itemUnit;
    @SerializedName("product_uuid")
    @Expose
    private String productUuid;
    @SerializedName("nearby_latilong")
    @Expose
    private String nearbyLatilong;
    @SerializedName("start_point_latilong")
    @Expose
    private Object startPointLatilong;
    @SerializedName("end_point_latilong")
    @Expose
    private Object endPointLatilong;
    @SerializedName("landmark_latilong")
    @Expose
    private Object landmarkLatilong;
    @SerializedName("destlandmark")
    @Expose
    private Object destlandmark;
    @SerializedName("destlandmark_latilong")
    @Expose
    private Object destlandmarkLatilong;
    @SerializedName("pbc_interest")
    @Expose
    private Integer pbcInterest;
    @SerializedName("buyer_cur_latilong")
    @Expose
    private Object buyerCurLatilong;
    @SerializedName("is_support_req")
    @Expose
    private Integer isSupportReq;
    @SerializedName("support_req_by")
    @Expose
    private Object supportReqBy;
    @SerializedName("is_buyer_exists")
    @Expose
    private Integer isBuyerExists;
    @SerializedName("support_status")
    @Expose
    private Integer supportStatus;
    @SerializedName("is_short_term")
    @Expose
    private Integer isShortTerm;
    @SerializedName("no_hrs")
    @Expose
    private Integer noHrs;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("parent_category_name")
    @Expose
    private String parentCategoryName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("requester_mobile")
    @Expose
    private String requesterMobile;
    @SerializedName("warrenty_required")
    @Expose
    private Integer warrentyRequired;
    @SerializedName("condition_required")
    @Expose
    private Integer conditionRequired;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBuysellCategoryId() {
        return buysellCategoryId;
    }

    public void setBuysellCategoryId(Integer buysellCategoryId) {
        this.buysellCategoryId = buysellCategoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Object getZipcode() {
        return zipcode;
    }

    public void setZipcode(Object zipcode) {
        this.zipcode = zipcode;
    }

    public Object getRadius() {
        return radius;
    }

    public void setRadius(Object radius) {
        this.radius = radius;
    }

    public String getDescisionFactor() {
        return descisionFactor;
    }

    public void setDescisionFactor(String descisionFactor) {
        this.descisionFactor = descisionFactor;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getLocationId() {
        return locationId;
    }

    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public Object getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(Object itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Object numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Integer getReqTotalVolume() {
        return reqTotalVolume;
    }

    public void setReqTotalVolume(Integer reqTotalVolume) {
        this.reqTotalVolume = reqTotalVolume;
    }

    public Object getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Object totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Object pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Object getDropOffDate() {
        return dropOffDate;
    }

    public void setDropOffDate(Object dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Object getNoOfBedRooms() {
        return noOfBedRooms;
    }

    public void setNoOfBedRooms(Object noOfBedRooms) {
        this.noOfBedRooms = noOfBedRooms;
    }

    public Object getSqFeet() {
        return sqFeet;
    }

    public void setSqFeet(Object sqFeet) {
        this.sqFeet = sqFeet;
    }

    public Object getFromDate() {
        return fromDate;
    }

    public void setFromDate(Object fromDate) {
        this.fromDate = fromDate;
    }

    public Object getToDate() {
        return toDate;
    }

    public void setToDate(Object toDate) {
        this.toDate = toDate;
    }

    public Object getItemCode() {
        return itemCode;
    }

    public void setItemCode(Object itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemSpec() {
        return itemSpec;
    }

    public void setItemSpec(Object itemSpec) {
        this.itemSpec = itemSpec;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getExpiryInfo() {
        return expiryInfo;
    }

    public void setExpiryInfo(Boolean expiryInfo) {
        this.expiryInfo = expiryInfo;
    }

    public Boolean getSellerExpiryInfo() {
        return sellerExpiryInfo;
    }

    public void setSellerExpiryInfo(Boolean sellerExpiryInfo) {
        this.sellerExpiryInfo = sellerExpiryInfo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Object getFlightType() {
        return flightType;
    }

    public void setFlightType(Object flightType) {
        this.flightType = flightType;
    }

    public Object getTravelType() {
        return travelType;
    }

    public void setTravelType(Object travelType) {
        this.travelType = travelType;
    }

    public Object getFlightDepartCity() {
        return flightDepartCity;
    }

    public void setFlightDepartCity(Object flightDepartCity) {
        this.flightDepartCity = flightDepartCity;
    }

    public Object getFlightDepartState() {
        return flightDepartState;
    }

    public void setFlightDepartState(Object flightDepartState) {
        this.flightDepartState = flightDepartState;
    }

    public Object getFlightDepartCountry() {
        return flightDepartCountry;
    }

    public void setFlightDepartCountry(Object flightDepartCountry) {
        this.flightDepartCountry = flightDepartCountry;
    }

    public Object getFlightArrivalCity() {
        return flightArrivalCity;
    }

    public void setFlightArrivalCity(Object flightArrivalCity) {
        this.flightArrivalCity = flightArrivalCity;
    }

    public Object getFlightArrivalState() {
        return flightArrivalState;
    }

    public void setFlightArrivalState(Object flightArrivalState) {
        this.flightArrivalState = flightArrivalState;
    }

    public Object getFlightArrivalCountry() {
        return flightArrivalCountry;
    }

    public void setFlightArrivalCountry(Object flightArrivalCountry) {
        this.flightArrivalCountry = flightArrivalCountry;
    }

    public Integer getFlightAdults() {
        return flightAdults;
    }

    public void setFlightAdults(Integer flightAdults) {
        this.flightAdults = flightAdults;
    }

    public Integer getFlightChildren() {
        return flightChildren;
    }

    public void setFlightChildren(Integer flightChildren) {
        this.flightChildren = flightChildren;
    }

    public Integer getFlightInfants() {
        return flightInfants;
    }

    public void setFlightInfants(Integer flightInfants) {
        this.flightInfants = flightInfants;
    }

    public Integer getFlightSeniors() {
        return flightSeniors;
    }

    public void setFlightSeniors(Integer flightSeniors) {
        this.flightSeniors = flightSeniors;
    }

    public Object getFlightOrginDatetime() {
        return flightOrginDatetime;
    }

    public void setFlightOrginDatetime(Object flightOrginDatetime) {
        this.flightOrginDatetime = flightOrginDatetime;
    }

    public Object getFlightReturnDatetime() {
        return flightReturnDatetime;
    }

    public void setFlightReturnDatetime(Object flightReturnDatetime) {
        this.flightReturnDatetime = flightReturnDatetime;
    }

    public Integer getFlightNoStops() {
        return flightNoStops;
    }

    public void setFlightNoStops(Integer flightNoStops) {
        this.flightNoStops = flightNoStops;
    }

    public Object getFlightPreferredAirlines() {
        return flightPreferredAirlines;
    }

    public void setFlightPreferredAirlines(Object flightPreferredAirlines) {
        this.flightPreferredAirlines = flightPreferredAirlines;
    }

    public Object getFlightPreferredClass() {
        return flightPreferredClass;
    }

    public void setFlightPreferredClass(Object flightPreferredClass) {
        this.flightPreferredClass = flightPreferredClass;
    }

    public Object getFlightPreferredTimeslot() {
        return flightPreferredTimeslot;
    }

    public void setFlightPreferredTimeslot(Object flightPreferredTimeslot) {
        this.flightPreferredTimeslot = flightPreferredTimeslot;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Object getBookCoverUrl() {
        return bookCoverUrl;
    }

    public void setBookCoverUrl(Object bookCoverUrl) {
        this.bookCoverUrl = bookCoverUrl;
    }

    public Object getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Object bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Object getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(Object bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public Integer getIsExtended() {
        return isExtended;
    }

    public void setIsExtended(Integer isExtended) {
        this.isExtended = isExtended;
    }

    public Integer getIsCustSupportEsclation() {
        return isCustSupportEsclation;
    }

    public void setIsCustSupportEsclation(Integer isCustSupportEsclation) {
        this.isCustSupportEsclation = isCustSupportEsclation;
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

    public Object getContactPref() {
        return contactPref;
    }

    public void setContactPref(Object contactPref) {
        this.contactPref = contactPref;
    }

    public Object getItemCatalogId() {
        return itemCatalogId;
    }

    public void setItemCatalogId(Object itemCatalogId) {
        this.itemCatalogId = itemCatalogId;
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

    public Integer getIsB2B() {
        return isB2B;
    }

    public void setIsB2B(Integer isB2B) {
        this.isB2B = isB2B;
    }

    public Object getCabStartingPoint() {
        return cabStartingPoint;
    }

    public void setCabStartingPoint(Object cabStartingPoint) {
        this.cabStartingPoint = cabStartingPoint;
    }

    public Object getCabDroppingPoint() {
        return cabDroppingPoint;
    }

    public void setCabDroppingPoint(Object cabDroppingPoint) {
        this.cabDroppingPoint = cabDroppingPoint;
    }

    public Object getLandmark() {
        return landmark;
    }

    public void setLandmark(Object landmark) {
        this.landmark = landmark;
    }

    public Object getCabStartDatetime() {
        return cabStartDatetime;
    }

    public void setCabStartDatetime(Object cabStartDatetime) {
        this.cabStartDatetime = cabStartDatetime;
    }

    public Object getCabBags() {
        return cabBags;
    }

    public void setCabBags(Object cabBags) {
        this.cabBags = cabBags;
    }

    public Object getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(Object typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public Object getAcNonAc() {
        return acNonAc;
    }

    public void setAcNonAc(Object acNonAc) {
        this.acNonAc = acNonAc;
    }

    public Object getCabNoOfDays() {
        return cabNoOfDays;
    }

    public void setCabNoOfDays(Object cabNoOfDays) {
        this.cabNoOfDays = cabNoOfDays;
    }

    public Object getCabTripDetails() {
        return cabTripDetails;
    }

    public void setCabTripDetails(Object cabTripDetails) {
        this.cabTripDetails = cabTripDetails;
    }

    public Object getCabBookingTime() {
        return cabBookingTime;
    }

    public void setCabBookingTime(Object cabBookingTime) {
        this.cabBookingTime = cabBookingTime;
    }

    public Object getCabEndDatetime() {
        return cabEndDatetime;
    }

    public void setCabEndDatetime(Object cabEndDatetime) {
        this.cabEndDatetime = cabEndDatetime;
    }

    public Object getCrmOpportinutyId() {
        return crmOpportinutyId;
    }

    public void setCrmOpportinutyId(Object crmOpportinutyId) {
        this.crmOpportinutyId = crmOpportinutyId;
    }

    public Object getCrmRevenueId() {
        return crmRevenueId;
    }

    public void setCrmRevenueId(Object crmRevenueId) {
        this.crmRevenueId = crmRevenueId;
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

    public String getLatilong() {
        return latilong;
    }

    public void setLatilong(String latilong) {
        this.latilong = latilong;
    }

    public Object getFlightDepartLatilong() {
        return flightDepartLatilong;
    }

    public void setFlightDepartLatilong(Object flightDepartLatilong) {
        this.flightDepartLatilong = flightDepartLatilong;
    }

    public Object getFlightArrivalLatilong() {
        return flightArrivalLatilong;
    }

    public void setFlightArrivalLatilong(Object flightArrivalLatilong) {
        this.flightArrivalLatilong = flightArrivalLatilong;
    }

    public Object getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Object itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getNearbyLatilong() {
        return nearbyLatilong;
    }

    public void setNearbyLatilong(String nearbyLatilong) {
        this.nearbyLatilong = nearbyLatilong;
    }

    public Object getStartPointLatilong() {
        return startPointLatilong;
    }

    public void setStartPointLatilong(Object startPointLatilong) {
        this.startPointLatilong = startPointLatilong;
    }

    public Object getEndPointLatilong() {
        return endPointLatilong;
    }

    public void setEndPointLatilong(Object endPointLatilong) {
        this.endPointLatilong = endPointLatilong;
    }

    public Object getLandmarkLatilong() {
        return landmarkLatilong;
    }

    public void setLandmarkLatilong(Object landmarkLatilong) {
        this.landmarkLatilong = landmarkLatilong;
    }

    public Object getDestlandmark() {
        return destlandmark;
    }

    public void setDestlandmark(Object destlandmark) {
        this.destlandmark = destlandmark;
    }

    public Object getDestlandmarkLatilong() {
        return destlandmarkLatilong;
    }

    public void setDestlandmarkLatilong(Object destlandmarkLatilong) {
        this.destlandmarkLatilong = destlandmarkLatilong;
    }

    public Integer getPbcInterest() {
        return pbcInterest;
    }

    public void setPbcInterest(Integer pbcInterest) {
        this.pbcInterest = pbcInterest;
    }

    public Object getBuyerCurLatilong() {
        return buyerCurLatilong;
    }

    public void setBuyerCurLatilong(Object buyerCurLatilong) {
        this.buyerCurLatilong = buyerCurLatilong;
    }

    public Integer getIsSupportReq() {
        return isSupportReq;
    }

    public void setIsSupportReq(Integer isSupportReq) {
        this.isSupportReq = isSupportReq;
    }

    public Object getSupportReqBy() {
        return supportReqBy;
    }

    public void setSupportReqBy(Object supportReqBy) {
        this.supportReqBy = supportReqBy;
    }

    public Integer getIsBuyerExists() {
        return isBuyerExists;
    }

    public void setIsBuyerExists(Integer isBuyerExists) {
        this.isBuyerExists = isBuyerExists;
    }

    public Integer getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(Integer supportStatus) {
        this.supportStatus = supportStatus;
    }

    public Integer getIsShortTerm() {
        return isShortTerm;
    }

    public void setIsShortTerm(Integer isShortTerm) {
        this.isShortTerm = isShortTerm;
    }

    public Integer getNoHrs() {
        return noHrs;
    }

    public void setNoHrs(Integer noHrs) {
        this.noHrs = noHrs;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRequesterMobile() {
        return requesterMobile;
    }

    public void setRequesterMobile(String requesterMobile) {
        this.requesterMobile = requesterMobile;
    }

    public Integer getWarrentyRequired() {
        return warrentyRequired;
    }

    public void setWarrentyRequired(Integer warrentyRequired) {
        this.warrentyRequired = warrentyRequired;
    }

    public Integer getConditionRequired() {
        return conditionRequired;
    }

    public void setConditionRequired(Integer conditionRequired) {
        this.conditionRequired = conditionRequired;
    }

}
