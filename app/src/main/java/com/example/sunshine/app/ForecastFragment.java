package com.example.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A forecast fragment containing a simple view.
 */
public class ForecastFragment extends Fragment
{
    private ArrayAdapter<String> m_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather()
    {
        String postal = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        new FetchWeatherTask(getContext(), m_adapter).execute(postal);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        m_adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                new ArrayList<String>());

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(m_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = m_adapter.getItem(position);
                openDetails(forecast);
            }
        });

        return rootView;
    }

    private void openDetails(String forecast)
    {
        Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(Intent.EXTRA_TEXT, forecast);
        startActivity(detailActivityIntent);
    }
}
