package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@RequiredArgsConstructor
public class CompetitionEvent {
   private EventType eventType;
   private Set<Competitor> Competitors;


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CompetitionEvent that = (CompetitionEvent) o;
      return eventType.equals(that.eventType);
   }

   @Override
   public int hashCode() {
      return Objects.hash(eventType);
   }
}
