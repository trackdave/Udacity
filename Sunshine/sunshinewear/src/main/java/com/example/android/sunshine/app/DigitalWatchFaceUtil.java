/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.app;

import android.graphics.Color;

import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;

public final class DigitalWatchFaceUtil {
    private static final String TAG = "DigitalWatchFaceUtil";
    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} background color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_BACKGROUND_COLOR = "BACKGROUND_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} hour digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_HOURS_COLOR = "HOURS_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} minute digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_MINUTES_COLOR = "MINUTES_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} second digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_SECONDS_COLOR = "SECONDS_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} second digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_HIGHTEMP_COLOR = "TEMP_HIGH_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} second digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_LOWTEMP_COLOR = "TEMP_LOW_COLOR";

    /**
     * The path for the {@link DataItem} containing {@link DigitalWatchFaceService} configuration.
     */
    public static final String WEATHER_PATH = "/weather-info";
    public static final String KEY_HIGH = "high";
    public static final String KEY_LOW = "low";
    public static final String KEY_WEATHER_ID = "weatherId";

    /**
     * Name of the default interactive mode hour digits color and the ambient mode hour digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_HOUR_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_HOUR_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_HOUR_DIGITS);

    /**
     * Name of the default interactive mode minute digits color and the ambient mode minute digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS);

    /**
     * Name of the default interactive mode second digits color and the ambient mode second digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_SECOND_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_SECOND_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_SECOND_DIGITS);

    /**
     * Name of the default interactive mode hour digits color and the ambient mode high temp digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_HIGH_TEMP_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_HIGH_TEMP_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_HIGH_TEMP_DIGITS);

    /**
     * Name of the default interactive mode hour digits color and the ambient mode low temp digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_LOW_TEMP_DIGITS = "Gray";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_LOW_TEMP_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_LOW_TEMP_DIGITS);

    public static int getWeatherIcon(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    /**
     * Callback interface to perform an action with the current config {@link DataMap} for
     * {@link DigitalWatchFaceService}.
     */

    private static int parseColor(String colorName) {
        return Color.parseColor(colorName.toLowerCase());
    }

    private DigitalWatchFaceUtil() { }
}
