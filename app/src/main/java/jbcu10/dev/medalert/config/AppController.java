package jbcu10.dev.medalert.config;

import android.app.Application;

import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Relative;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private Medicine medicine;
    private Relative relative;
    private int medicineId;
    private int firstAidId;
    private int relativeId;


    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public static AppController getmInstance() {
        return mInstance;
    }

    public static void setmInstance(AppController mInstance) {
        AppController.mInstance = mInstance;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public Relative getRelative() {
        return relative;
    }

    public void setRelative(Relative relative) {
        this.relative = relative;
    }

    public int getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(int relativeId) {
        this.relativeId = relativeId;
    }

    public int getFirstAidId() {
        return firstAidId;
    }

    public void setFirstAidId(int firstAidId) {
        this.firstAidId = firstAidId;
    }
}