load('realitymining.mat', 's');

columns = fieldnames(s);

delimiter = '\t';

for n = 59:106
    mkdir(int2str(n));
    
    for i = 1:size(columns, 1)
        
        if strcmp(columns{i}, 'places')
            mkdir(strcat(int2str(n), '/places'));
            try
                subcol = fieldnames(s(n).(columns{i}));

                for j = 1:size(subcol, 1)
                    subtable = array2table(s(n).(columns{i}).(subcol{j}));
                    writetable(subtable, strcat(int2str(n),'/places/',subcol{j},'.csv'), 'Delimiter', delimiter);
                end
            catch
                message = {'Fehler in Datensatz', int2str(n), 'in Spalte', columns{i}};
                disp(strjoin(message));
            end
        else
            try
                subtable = struct2table(s(n).(columns{i}));
                writetable(subtable, strcat(int2str(n),'/',columns{i},'.csv'), 'Delimiter', delimiter);
            catch
                try
                    subtable = array2table(s(n).(columns{i}));
                    writetable(subtable, strcat(int2str(n),'/',columns{i},'.csv'), 'Delimiter', delimiter);
                catch
                    message = {'Fehler in Datensatz', int2str(n), 'in Spalte', columns{i}};
                    disp(strjoin(message));
                end
            end
        end
    end
end

