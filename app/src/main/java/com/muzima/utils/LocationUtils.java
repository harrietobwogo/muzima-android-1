package com.muzima.utils;

import android.util.Log;
import com.muzima.MuzimaApplication;
import com.muzima.api.model.Location;
import com.muzima.api.model.LocationAttribute;
import com.muzima.api.model.LocationAttributeType;
import com.muzima.controller.LocationController;
import com.muzima.utils.Constants.Shr.KenyaEmr;

import java.util.UUID;

public class LocationUtils {
    private static final String TAG = LocationUtils.class.getSimpleName();
    public static String getLocationAttributeValue(Location location, String locationAttributeType){
        if(location != null){
            LocationAttribute locationAttribute = location.getAttribute(locationAttributeType);
            if(locationAttribute != null){
                return locationAttribute.getAttribute();
            }
        }
        return null;
    }

    public static String getKenyaEmrMasterFacilityListCode(Location location){
        String facilityCode = null;
        if(location != null){
            facilityCode = LocationUtils.getLocationAttributeValue(location, KenyaEmr.LocationAttributeType.MASTER_FACILITY_CODE.name);
            if(StringUtils.isEmpty(facilityCode)){
                facilityCode = LocationUtils.getLocationAttributeValue(location, KenyaEmr.LocationAttributeType.MASTER_FACILITY_CODE.uuid);
            }
        }
        return facilityCode;
    }

    public static Location getOrCreateDummyLocationByKenyaEmrMasterFacilityListCode(MuzimaApplication muzimaApplication, String facilityCode) throws Exception {
        LocationController locationController = muzimaApplication.getLocationController();
        Location location = null;
        LocationAttributeType locationAttributeType = null;
        try {
            locationAttributeType = locationController.getLocationAttributeByUuid(KenyaEmr.LocationAttributeType.MASTER_FACILITY_CODE.uuid);

            if (locationAttributeType != null) {
                location = locationController.getLocationByAttributeType(locationAttributeType, facilityCode);
            }
        } catch (LocationController.LocationLoadException e){
            Log.e(TAG, "Failed to get location",e);
        }

        if(location == null){
            location = new Location();
            location.setName("MFL " + facilityCode);
            location.setUuid(UUID.randomUUID().toString());

            if(locationAttributeType == null){
                locationAttributeType = new LocationAttributeType();
                locationAttributeType.setUuid(KenyaEmr.LocationAttributeType.MASTER_FACILITY_CODE.uuid);
                locationAttributeType.setName(KenyaEmr.LocationAttributeType.MASTER_FACILITY_CODE.name);
            }

            LocationAttribute locationAttribute = new LocationAttribute();
            locationAttribute.setAttribute(facilityCode);
            locationAttribute.setAttributeType(locationAttributeType);
            location.addAttribute(locationAttribute);
            try {
                locationController.saveLocation(location);
            } catch (LocationController.LocationSaveException e) {
                throw new Exception("Cannot save newly created identifier",e);
            }
        }
        return location;

    }
}
