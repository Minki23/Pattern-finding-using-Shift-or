import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static int shiftOr(String tekst, String wzorzec) {
        long[] maska_wzorca = new long[381];
        long R = ~1;
        int rozmiar_wzorca = wzorzec.length();

        if (rozmiar_wzorca > 63 || rozmiar_wzorca==0) {
            System.out.println("Błędny pattern");
            return -1;
        }
        for (int i = 0; i <= 380; ++i) {
            maska_wzorca[i] = ~0;
        }
        for (int i = 0; i < rozmiar_wzorca; ++i) {
            maska_wzorca[wzorzec.charAt(i)] &= ~(1L << i);
        }
        for (int i = 0; i < tekst.length(); ++i) {
            R |= maska_wzorca[tekst.charAt(i)];
            R <<= 1;
            if ((R & (1L << rozmiar_wzorca)) == 0)
                return i - rozmiar_wzorca + 1;
        }
        return -1;
    }

    public static ArrayList<Integer> findPatterns(String tekst, String pattern) {
        ArrayList<Integer> matches = new ArrayList<>();
        while (shiftOr(tekst, pattern) != -1) {
            int position = shiftOr(tekst, pattern);
            matches.add((position + matches.size()));
            ArrayList<String> split = new ArrayList<>(Arrays.asList(tekst.split("")));
            for (int i = 0; i < pattern.length(); i++)
                split.remove(position);
            StringBuilder tBuilder = new StringBuilder();
            for (String znak : split) {
                tBuilder.append(znak);
            }
            tekst = tBuilder.toString();
        }
        return matches;
    }

    public static String ZmianyWZnalezionych(String tekst, String pattern) {
        ArrayList<Integer> znalezione = findPatterns(tekst, pattern);
        if(znalezione.size()!=0) {
            ArrayList<String> splited;
            for (Integer integer : znalezione) {
                int poczatek, koniec;
                int miejsce = integer;
                while (true) {
                    if (String.valueOf(tekst.charAt(miejsce)).equals(" ")) {
                        poczatek = miejsce + 1;
                        break;
                    } else if (miejsce == 0) {
                        poczatek = miejsce;
                        break;
                    } else
                        miejsce--;
                }
                char c = tekst.charAt(poczatek);
                if (c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                    miejsce = integer;
                    c = tekst.charAt(miejsce + 1);
                    if (c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                        while (true) {
                            if (String.valueOf(tekst.charAt(miejsce)).equals(" ") || String.valueOf(tekst.charAt(miejsce)).equals(",")||String.valueOf(tekst.charAt(miejsce)).equals(".")) {
                                koniec = miejsce - 1;
                                break;
                            } else if (miejsce == tekst.length() - 1) {
                                koniec = miejsce;
                                break;
                            } else
                                miejsce++;
                        }
                        splited = new ArrayList<>(List.of(tekst.split("")));
                        StringBuilder wyraz1 = new StringBuilder();
                        StringBuilder wyraz2 = new StringBuilder();
                        for (int i1 = 0; i1 < integer - poczatek; i1++) {
                            wyraz1.append(splited.get(poczatek + i1));
                        }
                        for (int i2 = 0; i2 < koniec - integer; i2++) {
                            wyraz2.append(splited.get(integer + 1 + i2));
                        }
                        String wynik = wyraz2 + pattern + wyraz1;
                        for (int k = 0; k < (koniec - poczatek) + 1; k++) {
                            splited.remove(poczatek);
                        }
                        for (int k1 = 0; k1 < koniec - poczatek + 1; k1++) {
                            splited.add(poczatek + k1, wynik.split("")[k1]);
                        }
                        StringBuilder tBuilder = new StringBuilder();
                        for (String s : splited) {
                            tBuilder.append(s);
                        }
                        tekst = tBuilder.toString();
                    }
                }
            }
        }
        return tekst;
    }

    public static void main(String[] args) {
        String inwokacja= """
                Litwo! Ojczyzno moja! ty jesteś jak zdrowie.
                             
                Ile cię trzeba cenić, ten tylko się dowie,
                                
                Kto cię stracił. Dziś piękność twą w całej ozdobie
                                
                Widzę i opisuję, bo tęsknię po tobie.
                                
                Panno Święta, co Jasnej bronisz Częstochowy
                                
                I w Ostrej świecisz Bramie! Ty, co gród zamkowy
                                
                Nowogródzki ochraniasz z jego wiernym ludem!
                                
                Jak mnie dziecko do zdrowia powróciłaś cudem
                                
                (Gdy od płaczącej matki pod Twoję opiekę
                                
                Ofiarowany, martwą podniosłem powiekę
                                
                I zaraz mogłem pieszo do Twych świątyń progu
                                
                Iść za wrócone życie podziękować Bogu),
                                
                Tak nas powrócisz cudem na Ojczyzny łono.
                                
                Tymczasem przenoś moję duszę utęsknioną
                                
                Do tych pagórków leśnych, do tych łąk zielonych,
                                
                Szeroko nad błękitnym Niemnem rozciągnionych;
                               
                Do tych pól malowanych zbożem rozmaitem,
                                
                Wyzłacanych pszenicą, posrebrzanych żytem;
                                
                Gdzie bursztynowy świerzop, gryka jak śnieg biała,
                                
                Gdzie panieńskim rumieńcem dzięcielina pała,
                                
                A wszystko przepasane, jakby wstęgą, miedzą
                                
                Zieloną, na niej z rzadka ciche grusze siedzą.
                """;
        String tekst = "AA=BC A=b. A=_b A=1 a=B _A=B 1=B A=B Litw0=0jczyzno moja, Ty jestes jak zdr0w13, ile C13=c3n1c, t3n ty1k0 si3 d0wie=_kt0 C13 stracil.";
        String pattern = "=";
        System.out.println("Znaleziono " + findPatterns(tekst, pattern).size() + " instancji ('" + pattern + "') w podanym tekście na indeksach "+findPatterns(tekst,pattern));
        System.out.println(ZmianyWZnalezionych(tekst, pattern));
    }
}
//zlozonosc O(m+n)