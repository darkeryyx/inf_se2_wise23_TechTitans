package group.artifact.services;

import group.artifact.dtos.OfferDTO;
import group.artifact.dtos.impl.OfferDTOImpl;
import group.artifact.entities.Offer;
import group.artifact.repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class OfferService {

    private OfferRepository offerRepository;

    public void createOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public List<OfferDTO> getAllOffersAndTheirCompany(){
        List<Offer> offers = offerRepository.findAll(); // all offers needs to be parsed to DTO
        ArrayList<OfferDTO> offerDTOS = new ArrayList<>(); // this whill be done by an array list
        for (Offer offer : offers) {
            OfferDTO dto = new OfferDTOImpl();
            dto.setCompanyName(offer.getCompany().getName());
            dto.setJob(offer.getJob());
            dto.setBusiness(offer.getBusiness());
            dto.setDescription(offer.getDescription());
            dto.setIncomePerHour(offer.getIncomePerHour());
            offerDTOS.add(dto);
        }
        return offerDTOS;
    }
    public List<String> getBusinessList() {
        return Arrays.asList(
                "Fischerei & Aquakultur",
                "Forstwirtschaft",
                "Landwirtschaft",
                "Ausbaugewerbe",
                "Bauhauptgewerbe",
                "Chemieindustrie",
                "Fossile Rohstoffe",
                "Glas-, Keramik- & Kunststoffindustrie",
                "Mineralische Rohstoffe & Bergbau",
                "Mineralöl & Raffinierung",
                "Nachwachsende Rohstoffe",
                "Papierindustrie & Zellstoffindustrie",
                "Business Service",
                "Handwerk",
                "Haushaltsnahe Dienstleistungen",
                "B2B-E-Commerce",
                "B2C-E-Commerce",
                "C2C-E-Commerce",
                "Abfallwirtschaft",
                "Emissionen",
                "Energie",
                "Klimawandel, Wetter & Natur",
                "Umwelt- & Energietechnik",
                "Wasserwirtschaft",
                "Banken & Finanzdienstleistungen",
                "Finanzmarkt",
                "Immobilien",
                "Versicherungen",
                "Freizeitparks & Tierparks",
                "Glücksspiel",
                "Hobby & Freizeitverhalten",
                "Kunst, Kultur & Events",
                "Bildung & Forschung",
                "Demographie",
                "Geographie",
                "Geschichte",
                "Kriminalität, Recht & Justiz",
                "Regionaldaten zur Gesellschaft",
                "Religion",
                "Soziales",
                "Verbände & Organisationen",
                "DIY-Handel (Bau & Heimwerker)",
                "Einkaufsverhalten",
                "Einzelhandel",
                "Elektrofachhandel",
                "Großhandel",
                "Hobby und Freizeit",
                "Körperpflege & Gesundheit",
                "Lebensmittelhandel",
                "Möbelhandel & Einrichtung",
                "Shopping Center & FOC",
                "Textilhandel, Schuhhandel & Accessoires",
                "Cyberkriminalität",
                "Demographie & Nutzung",
                "E-Government",
                "Mobiles Internet & Apps",
                "Reichweite & Traffic",
                "Social Media",
                "Streaming",
                "Suchmaschinen & SEO",
                "Alkoholische Getränke",
                "Garten & Heimwerken",
                "Haustiere & zoologischer Bedarf",
                "Kleidung, Schuhe & Textilien",
                "Kosmetik & Körperpflege",
                "Lebensmittel & Ernährung",
                "Markt-Media-Daten & Konsumverhalten",
                "Möbel, Einrichtung & Hausrat",
                "Nicht-alkoholische Getränke",
                "Spielwaren & Spiele",
                "Tabakwaren",
                "Waschmittel & Reinigungsmittel",
                "Familie, Freunde & Bekannte",
                "Feste & Feiertage",
                "Liebe & Partnerschaft",
                "Persönlichkeit & Verhalten",
                "Prominente & Celebrities",
                "Sexualität",
                "Mediennutzung",
                "Musik & Musikindustrie",
                "Rundfunk, TV & Film",
                "Verlagswesen & Buchmarkt",
                "Videospiele & Gaming",
                "Elektroindustrie",
                "Feinmechanik & Optik",
                "Kraftfahrzeugbau",
                "Luft- & Raumfahrzeugbau",
                "Maschinenbau & Anlagenbau",
                "Metallindustrie",
                "Rüstungsindustrie",
                "Schienenfahrzeugbau",
                "Schiffbau",
                "Ärzte, Krankenhäuser & Apotheken",
                "Gesundheitssystem",
                "Gesundheitszustand",
                "Medizintechnik",
                "Pflege & Betreuung",
                "Pharmaindustrie & Pharmaprodukte",
                "Freizeitsport",
                "Sportereignisse",
                "Sportergebnisse",
                "Sportvermarktung",
                "Wellness & Fitness",
                "Fachbereich Deutsch & Medienkunde",
                "Fachbereich Geografie",
                "Fachbereich Geschichte",
                "Fachbereich MINT",
                "Fachbereich Politik & Gesellschaft",
                "Fachbereich Religion, Philosophie & Ethik",
                "Fachbereich Wirtschaft",
                "Fernsehempfang",
                "Haushaltsgeräte",
                "IT-Hardware",
                "IT-Services",
                "Künstliche Intelligenz",
                "Software",
                "Telekommunikation",
                "Unterhaltungselektronik",
                "Gastronomie",
                "Geschäftsreisen",
                "Privatreisen",
                "Reisemittler & Reiseveranstalter",
                "Unterkünfte",
                "Bahn- & Schienenverkehr",
                "Fahrzeuge & Straßenverkehr",
                "Logistik & Transport",
                "Luftfahrt & Raumfahrt",
                "Öffentlicher Personennahverkehr",
                "Schifffahrt",
                "Verkehrsinfrastruktur & Tankstellen",
                "Marketing",
                "Werbung",
                "Arbeit & Beruf",
                "Konjunktur & Wirtschaft",
                "Kriege & Konflikte",
                "Öffentliche Verwaltung",
                "Politik",
                "Regionaldaten zu Wirtschaft & Politik",
                "Steuern & Staatsfinanzen",
                "Verteidigung"
        );
    }
}
