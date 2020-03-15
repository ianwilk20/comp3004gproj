package fooderie.CookingAssistant.views.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import fooderie.models.FooderieRepository;

public class CookingAssistantViewerViewModel extends AndroidViewModel
{
    private FooderieRepository m_repo;

    public CookingAssistantViewerViewModel(Application application)
    {
        super(application);
        m_repo = new FooderieRepository(application);
    }


}
