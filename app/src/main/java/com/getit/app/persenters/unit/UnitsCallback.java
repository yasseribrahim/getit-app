package com.getit.app.persenters.unit;

import com.getit.app.models.Unit;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface UnitsCallback extends BaseCallback {
    default void onGetUnitsComplete(List<Unit> units) {
    }

    default void onGetUnitsCountComplete(long count) {
    }

    default void onSaveUnitComplete() {
    }

    default void onDeleteUnitComplete(Unit unit) {
    }

    default void onGetUnitComplete(Unit unit) {
    }
}
