package me.quartz.hungergames.profile;

import java.util.*;

public class ProfileManager {
    private final List<Profile> profiles;

    public ProfileManager() {
        this.profiles = new ArrayList<>();
    }

    public Profile getProfile(UUID uuid) {
        Optional<Profile> profile = profiles.stream().filter(profile1 -> profile1.getUuid().toString().equals(uuid.toString())).findAny();
        if(!profile.isPresent()) {
            Profile profileNotNull = new Profile(uuid);
            profiles.add(profileNotNull);
            return profileNotNull;
        }
        return profile.orElse(null);
    }
}
