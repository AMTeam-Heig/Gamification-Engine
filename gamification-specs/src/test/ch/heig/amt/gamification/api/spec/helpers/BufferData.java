package ch.heig.amt.gamification.api.spec.helpers;

import ch.heig.amt.gamification.api.dto.Application;
import ch.heig.amt.gamification.api.dto.NewApplication;
import lombok.Getter;
import lombok.Setter;

public class BufferData {
    @Getter @Setter
    static Application app;
    @Getter @Setter
    NewApplication tempApp;

}