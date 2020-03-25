package fooderie.CookingAssistant.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import fooderie._main.models.FooderieRepository;

public class CookingAssistantViewerViewModel extends AndroidViewModel
{
    private FooderieRepository m_repo;

    public CookingAssistantViewerViewModel(Application application)
    {
        super(application);
        m_repo = new FooderieRepository(application);
    }


}
