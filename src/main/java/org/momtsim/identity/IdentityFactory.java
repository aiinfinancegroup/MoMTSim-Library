package org.momtsim.identity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Provides deterministic synthetic identities for simulated actors.
 */
public class IdentityFactory {
    private static final String[] FIRST_NAMES = {
            "Avery", "Blake", "Casey", "Devon", "Emery", "Finley", "Harper", "Jordan",
            "Kai", "Logan", "Morgan", "Parker", "Quinn", "Reese", "Riley", "Taylor"
    };
    private static final String[] LAST_NAMES = {
            "Adams", "Bennett", "Carter", "Diaz", "Evans", "Foster", "Garcia", "Hayes",
            "Ibrahim", "Jones", "Kato", "Lewis", "Mensah", "Nakamya", "Okello", "Patel"
    };
    private static final String[] COMPANY_PREFIXES = {
            "Apex", "Bridge", "Crown", "Delta", "Equator", "Frontier", "Horizon", "Keystone",
            "Liberty", "Metro", "Nile", "Orbit", "Pioneer", "Summit", "Unity", "Vertex"
    };
    private static final String[] COMPANY_SUFFIXES = {
            "Trading", "Stores", "Exchange", "Markets", "Services", "Logistics", "Ventures", "Supplies"
    };

    private final Set<String> ccnSet = new HashSet<>();
    private final Random random;
    private long personCounter = 0;
    private long merchantCounter = 0;
    private long vatCounter = 0;

    public IdentityFactory(int randomSeed) {
        random = new Random(randomSeed);
    }

    protected String addSuffixToEmail(String email, String suffix) {
        String[] parts = email.split("@");
        assert parts.length == 2;
        return parts[0] + suffix + "@" + parts[1];
    }

    public ClientIdentity nextPerson() {
        long sequence = ++personCounter;
        String firstName = pick(FIRST_NAMES);
        String lastName = pick(LAST_NAMES);
        String fullName = firstName + " " + lastName;
        String suffix = String.format("%03d", random.nextInt(100));
        String email = addSuffixToEmail(
                String.format("%s.%s@example.com", firstName.toLowerCase(), lastName.toLowerCase()),
                suffix
        );

        return new ClientIdentity(
                getNextCreditCard(),
                fullName,
                email,
                String.format("%09d", sequence),
                String.format("+1-555-%03d-%04d", random.nextInt(1000), sequence % 10_000));
    }

    public BankIdentity nextBank() {
        return new BankIdentity(getNextVAT(), String.format("Bank of %s", pick(LAST_NAMES)));
    }

    public MerchantIdentity nextMerchant() {
        return new MerchantIdentity(String.format("M%012d", ++merchantCounter), nextMerchantName());
    }

    public String getNextVAT() {
        return String.format("VAT%012d", ++vatCounter);
    }

    public String getNextCreditCard() {
        String ccn = String.format("4%015d", ccnSet.size() + 1L);
        while (!ccnSet.add(ccn)) {
            ccn = String.format("4%015d", ccnSet.size() + 1L);
        }
        return ccn;
    }

    public String nextMerchantName() {
        return String.format("%s %s", pick(COMPANY_PREFIXES), pick(COMPANY_SUFFIXES));
    }

    private String pick(String[] values) {
        return values[random.nextInt(values.length)];
    }
}
