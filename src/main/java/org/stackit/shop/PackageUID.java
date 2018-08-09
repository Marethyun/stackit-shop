package org.stackit.shop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PackageUID {

    public static final Pattern REGEX = Pattern.compile("^[A-Z0-9]{8}$");

    private String uid;

    public PackageUID(String uid) {

        uid = uid.toUpperCase();

        Matcher matcher = REGEX.matcher(uid);

        if (matcher.matches()){
            this.uid = uid;
        } else {
            throw new MalformedUIDException();
        }
    }

    @Override
    public String toString() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PackageUID && obj.toString().equalsIgnoreCase(this.toString());
    }

    public class MalformedUIDException extends StackItShopException {}
}
