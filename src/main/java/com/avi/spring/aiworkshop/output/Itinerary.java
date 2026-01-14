package com.avi.spring.aiworkshop.output;

import java.util.List;

public record Itinerary(List<Activity> activities, List<Accommodation> accommodations) {
}
